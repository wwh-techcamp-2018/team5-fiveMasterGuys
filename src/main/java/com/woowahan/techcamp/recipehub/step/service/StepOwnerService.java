package com.woowahan.techcamp.recipehub.step.service;

import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.common.support.Message;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.OfferType;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.step.repository.StepOfferRepository;
import com.woowahan.techcamp.recipehub.step.repository.StepRepository;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class StepOwnerService implements StepService {

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private StepOfferRepository stepOfferRepository;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor messageSourceAccessor;

    @Override
    @Transactional
    public Step create(User user, StepCreationDTO dto, Recipe recipe) {
        Long sequence = getNextSequence(dto);

        stepRepository.increaseSequenceGte(recipe, sequence);

        return stepRepository.save(
                dto.toStep(user, recipe, sequence)
        );
    }

    @Override
    @Transactional
    public Step modify(User user, long targetId, StepCreationDTO dto, Recipe recipe) {
        Step previousStep = findStepById(targetId);
        previousStep.close();

        Step modifiedStep = stepRepository.save(dto.toStep(user, recipe, previousStep.getSequence()));

        stepOfferRepository.rejectModifyingOfferByTarget(previousStep);
        stepOfferRepository.changeAppendOffersTarget(previousStep, modifiedStep);
        return modifiedStep;
    }

    @Override
    @Transactional
    public Step approve(Recipe recipe, Long offerId, User user) {
        Optional<Step> maybeApprovedStep = Optional.empty();
        StepOffer offer = stepOfferRepository.findById(offerId)
                .orElseThrow(EntityNotFoundException::new);

        if (offer.getTarget() != null && offer.getTarget().isClosed()) {
            throw new BadRequestException(messageSourceAccessor.getMessage(Message.STEP_CLOSED));
        }

        if (offer.getOfferType().equals(OfferType.APPEND)) {
            return approveAppend(offer, recipe);
        }

        if (offer.getOfferType().equals(OfferType.MODIFY)) {
            Step approvedStep = approveModify(offer);
            stepOfferRepository.changeAppendOffersTarget(offer.getTarget(), approvedStep);
            return approvedStep;
        }

        return maybeApprovedStep.orElseThrow(EntityNotFoundException::new);
    }

    private Step approveAppend(StepOffer offer, Recipe recipe) {
        Long sequence = getSequenceFromOffer(offer);
        stepRepository.increaseSequenceGte(recipe, sequence);

        rejectOffersByTarget(offer.getTarget(), recipe);
        stepOfferRepository.approveStepOffer(offer.getId(), sequence);
        return findStepById(offer.getId());
    }

    private Step approveModify(StepOffer offer) {
        Step targetStep = offer.getTarget();
        Long targetSequence = targetStep.getSequence();

        targetStep.close();
        Step closedStep = stepRepository.save(targetStep);

        stepOfferRepository.approveStepOffer(offer.getId(), targetSequence);
        stepOfferRepository.rejectModifyingOfferByTarget(closedStep);

        return findStepById(offer.getId());
    }

    private Long getNextSequence(StepCreationDTO dto) {
        return dto.getTargetStepId() == null
                ? 1
                : findStepById(dto.getTargetStepId()).getSequence() + 1;
    }

    private Long getSequenceFromOffer(StepOffer stepOffer) {
        return stepOffer.getTarget() == null
                ? 1L
                : stepOffer.getTarget().getSequence() + 1L;
    }

    private void rejectOffersByTarget(Step target, Recipe recipe) {
        if (target == null) {
            stepOfferRepository.rejectAppendingOffersByNullTarget(recipe);
        }

        stepOfferRepository.rejectAppendingOffersByTarget(target);
    }

    private Step findStepById(Long id) {
        return stepRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
