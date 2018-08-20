package com.woowahan.techcamp.recipehub.recipestep.controller;

import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import com.woowahan.techcamp.recipehub.recipestep.domain.AbstractRecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.recipestep.service.RecipeStepServiceProvider;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes/{recipeId}/steps")
public class RecipeStepController {
    @Autowired
    private RecipeStepServiceProvider recipeStepServiceProvider;

    @Autowired
    private RecipeService recipeService;

    @AuthRequired
    @PostMapping("")
    public RestResponse<AbstractRecipeStep> create(User user, RecipeStepCreationDTO dto) {
        Recipe recipe = recipeService.findById(dto.getRecipeId());
        return RestResponse.success(
                recipeStepServiceProvider.getService(recipe, user).create(user, dto, recipe)
        );
    }
}
