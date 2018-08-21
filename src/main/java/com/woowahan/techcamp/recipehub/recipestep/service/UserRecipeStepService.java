package com.woowahan.techcamp.recipehub.recipestep.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStepRequest;
import com.woowahan.techcamp.recipehub.recipestep.domain.RequestType;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserRecipeStepService implements RecipeStepService {


    @Autowired
    private RecipeStepRequestRepository recipeStepRequestRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Override
    public RecipeStepRequest create(User user, RecipeStepCreationDTO dto, Recipe recipe) {
        RecipeStep step = null;

        if (dto.getPreviousStepId() != null) {
            step = recipeStepRepository.findById(dto.getPreviousStepId()).orElseThrow(EntityNotFoundException::new);
        }

        return recipeStepRequestRepository.save(RecipeStepRequest.from(user, dto, recipe, step, RequestType.APPEND));
    }
}
