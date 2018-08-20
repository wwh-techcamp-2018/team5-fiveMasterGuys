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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/recipes/{recipeId}/steps")
public class RecipeStepRestController {
    @Autowired
    private RecipeStepServiceProvider provider;

    @Autowired
    private RecipeService recipeService;

    @AuthRequired
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse<AbstractRecipeStep> create(User user, @Valid @RequestBody RecipeStepCreationDTO dto) {
        Recipe recipe = recipeService.findById(dto.getRecipeId());
        AbstractRecipeStep data = provider.getService(recipe, user).create(user, dto, recipe);
        return RestResponse.success(data);
    }
}
