package com.woowahan.techcamp.recipehub.step.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
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

    @Override
    @Transactional
    public Step create(User user, StepCreationDTO dto, Recipe recipe) {
        Long sequence = dto.getPreviousStepId() == null
                ? 1
                : findById(dto.getPreviousStepId()).getSequence() + 1;

        stepRepository.increaseSequenceGte(recipe, sequence);

        return stepRepository.save(
                Step.builder()
                        .recipe(recipe)
                        .imgUrl(dto.getImgUrl())
                        .content(dto.getContent())
                        .name(dto.getName())
                        .ingredients(null)
                        .sequence(sequence)
                        .closed(false)
                        .writer(user)
                        .build()
        );
    }

    private Step findById(Long id) {
        return stepRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
