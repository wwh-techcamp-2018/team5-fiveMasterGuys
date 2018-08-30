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

@Service
public class StepOfferService implements StepService {


    @Autowired
    private StepOfferRepository stepOfferRepository;

    @Autowired
    private StepRepository stepRepository;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor messageSourceAccessor;

    @Override
    public StepOffer create(User user, StepCreationDTO dto, Recipe recipe) {
        Step step = null;

        if (dto.getTargetStepId() != null) {
            step = stepRepository.findById(dto.getTargetStepId()).orElseThrow(EntityNotFoundException::new);
        }

        return stepOfferRepository.save(StepOffer.from(user, dto, recipe, step, OfferType.APPEND));
    }

    @Override
    @Transactional
    public StepOffer modify(User user, long targetId, StepCreationDTO dto, Recipe recipe) {
        Step targetStep = stepRepository.findById(targetId).orElseThrow(EntityNotFoundException::new);
        if (targetStep.isClosed()) {
            throw new BadRequestException(messageSourceAccessor.getMessage(Message.STEP_CLOSED));
        }
        return stepOfferRepository.save(StepOffer.from(user, dto, recipe, targetStep, OfferType.MODIFY));
    }

    @Override
    public Step approve(Recipe recipe, Long offerId, User user) {
        return null;
    }
}
