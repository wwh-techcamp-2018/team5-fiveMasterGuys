package com.woowahan.techcamp.recipehub.step.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.OfferType;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.step.repository.StepOfferRepository;
import com.woowahan.techcamp.recipehub.step.repository.StepRepository;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class StepOwnerService implements StepService {

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private StepOfferRepository stepOfferRepository;

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
        StepOffer offer = stepOfferRepository.findById(offerId)
                .orElseThrow(EntityNotFoundException::new);

        if (offer.getOfferType().equals(OfferType.APPEND)) {
            approveAppend(offer, recipe);
        }

        if (offer.getOfferType().equals(OfferType.MODIFY)) {
            approveModify(offer);
        }

        return findStepById(offerId);
    }

    private void approveAppend(StepOffer offer, Recipe recipe) {
        Long sequence = getSequenceFromOffer(offer);
        stepRepository.increaseSequenceGte(recipe, sequence);

        rejectOffersByTarget(offer.getTarget(), recipe);
        stepOfferRepository.approveStepOffer(offer.getId(), sequence);
    }

    private void approveModify(StepOffer offer) {
        Step targetStep = offer.getTarget();
        Long targetSequence = targetStep.getSequence();

        targetStep.close();
        stepRepository.save(targetStep);

        stepOfferRepository.approveStepOffer(offer.getId(), targetSequence);
        stepOfferRepository.rejectModifyingOfferByTarget(targetStep);
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
