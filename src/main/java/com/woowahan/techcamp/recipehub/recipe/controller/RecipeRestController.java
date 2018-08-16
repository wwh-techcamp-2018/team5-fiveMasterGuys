package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
public class RecipeRestController {

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Recipe create(){
        return new Recipe();
    }
}
