package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeCreationDTO;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes")
public class RecipeRestController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Recipe create(User user, RecipeCreationDTO dto) {
        return recipeService.create(user, dto);
    }
}
