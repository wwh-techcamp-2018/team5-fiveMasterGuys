package com.woowahan.techcamp.recipehub.recipestep.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStepRepository;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class OwnerRecipeStepService implements RecipeStepService {

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Override
    @Transactional
    public RecipeStep create(User user, RecipeStepCreationDTO dto, Recipe recipe) {
        Long sequence = dto.getPreviousStepId() == null
                ? 1
                : findById(dto.getPreviousStepId()).getSequence() + 1;

        recipeStepRepository.increaseSequenceGte(recipe, sequence);

        return recipeStepRepository.save(
                RecipeStep.builder()
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

    private RecipeStep findById(Long id) {
        return recipeStepRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
