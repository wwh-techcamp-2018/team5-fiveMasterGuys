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

import javax.persistence.EntityNotFoundException;

@Service
public class StepOfferService implements StepService {


    @Autowired
    private StepOfferRepository stepOfferRepository;

    @Autowired
    private StepRepository stepRepository;

    @Override
    public StepOffer create(User user, StepCreationDTO dto, Recipe recipe) {
        Step step = null;

        if (dto.getTargetStepId() != null) {
            step = stepRepository.findById(dto.getTargetStepId()).orElseThrow(EntityNotFoundException::new);
        }

        return stepOfferRepository.save(StepOffer.from(user, dto, recipe, step, OfferType.APPEND));
    }

    @Override
    public StepOffer modify(User user, long targetId, StepCreationDTO dto, Recipe recipe) {
        Step targetStep = stepRepository.findById(targetId).orElseThrow(EntityNotFoundException::new);
        return stepOfferRepository.save(StepOffer.from(user, dto, recipe, targetStep, OfferType.MODIFY));
    }
}
