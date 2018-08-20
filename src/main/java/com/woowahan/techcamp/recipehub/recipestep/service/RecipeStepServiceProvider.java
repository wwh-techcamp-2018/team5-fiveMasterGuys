package com.woowahan.techcamp.recipehub.recipestep.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecipeStepServiceProvider {

    @Autowired
    private OwnerRecipeStepService ownerRecipeStepService;

    @Autowired
    private UserRecipeStepService userRecipeStepService;

    public RecipeStepService getService(Recipe recipe, User user) {
        if (recipe.isOwner(user)) {
            return ownerRecipeStepService;
        }
        return userRecipeStepService;
    }
}
