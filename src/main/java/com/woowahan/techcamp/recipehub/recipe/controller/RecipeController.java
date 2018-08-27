package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.service.CategoryService;
import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeDTO;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeResponseDTO;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private static final String TEMPLATE_RECIPE_CREATE = "recipe/create";
    private static final String TEMPLATE_RECIPE_DETAIL = "recipe/recipe";
    private static final String RECIPE_KEY = "recipe";
    private static final String FIRST_OFFERS = "firstOffers";

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/create")
    @AuthRequired
    public String get(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return TEMPLATE_RECIPE_CREATE;
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute(RECIPE_KEY, RecipeResponseDTO.from(recipe));
        model.addAttribute("completed", recipe.isCompleted());

        if (!recipe.isCompleted()) {
            model.addAttribute(FIRST_OFFERS, recipeService.findNullTargetStepOffersByRecipe(recipe));
        }

        return TEMPLATE_RECIPE_DETAIL;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.MOVED_PERMANENTLY)
    @AuthRequired
    public String create(User owner, @Valid RecipeDTO dto) {
        Recipe recipe = recipeService.create(owner, dto);
        return "redirect:/recipes/" + recipe.getId();
    }
}
