package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeCreationDTO;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/recipes")
public class RecipeRestController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    @AuthRequired
    @ResponseStatus(code = HttpStatus.CREATED)
    public RestResponse<Recipe> create(User user, @Valid @RequestBody RecipeCreationDTO dto) {
        return RestResponse.success(recipeService.create(user, dto));
    }
}
