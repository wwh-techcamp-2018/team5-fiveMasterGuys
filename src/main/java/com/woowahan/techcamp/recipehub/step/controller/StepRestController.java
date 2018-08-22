package com.woowahan.techcamp.recipehub.step.controller;

import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import com.woowahan.techcamp.recipehub.step.domain.AbstractStep;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.step.service.StepServiceProvider;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/recipes/{recipeId}/steps")
public class StepRestController {
    @Autowired
    private StepServiceProvider provider;

    @Autowired
    private RecipeService recipeService;


    @AuthRequired
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse<AbstractStep> create(@PathVariable("recipeId") long recipeId, User user, @Valid @RequestBody StepCreationDTO dto) {
        Recipe recipe = recipeService.findById(recipeId);
        return RestResponse.success(
                provider.getService(recipe, user).create(user, dto, recipe)
        );
    }

}
