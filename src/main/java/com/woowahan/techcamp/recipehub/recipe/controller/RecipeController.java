package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeCreationDTO;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeDetailDTO;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private static final String RECIPE_COMPLETED = "/recipe-completed";
    private static final String RECIPE_INCOMPLETED = "/recipe-incompleted";
    private static final String RECIPE_KEY = "recipe";

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute(RECIPE_KEY, RecipeDetailDTO.from(recipe));

        return recipe.isCompleted() ? RECIPE_COMPLETED : RECIPE_INCOMPLETED;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @AuthRequired
    public String create(User owner, @Valid RecipeCreationDTO dto) {
        Recipe recipe = recipeService.create(owner, dto);
        return "redirect:/recipes/" + recipe.getId();
    }
}
