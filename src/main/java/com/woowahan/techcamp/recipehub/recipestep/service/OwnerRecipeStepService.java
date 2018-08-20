package com.woowahan.techcamp.recipehub.recipestep.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class OwnerRecipeStepService implements RecipeStepService {
    @Override
    public RecipeStep create(User user, RecipeStepCreationDTO dto, Recipe recipe) {
        return null;
    }
}
