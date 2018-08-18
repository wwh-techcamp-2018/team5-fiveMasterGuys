package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeResponseDTO;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute(RECIPE_KEY, RecipeResponseDTO.from(recipe));
        if (recipe.isCompleted()) {
            return RECIPE_COMPLETED;
        }

        return RECIPE_INCOMPLETED;
    }
}
