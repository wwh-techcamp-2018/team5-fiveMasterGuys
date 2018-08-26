package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.service.CategoryService;
import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.common.security.SessionUtils;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private static final String RECIPE_CREATE = "recipe/create";
    private static final String RECIPE_COMPLETED = "recipe/completed";
    private static final String RECIPE_INCOMPLETED = "recipe/incompleted";
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
        return RECIPE_CREATE;
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Long id, Model model, HttpSession session) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute(RECIPE_KEY, RecipeResponseDTO.from(recipe));

        SessionUtils.getUserFromSession(session)
                .ifPresent(user -> {
                    if (recipe.isOwner(user) && !recipe.isCompleted()) {
                        model.addAttribute("authorizedUser", true);
                    }
                });

        if (!recipe.isCompleted()) {
            model.addAttribute(FIRST_OFFERS, recipeService.findNullTargetStepOffersByRecipe(recipe));
        }

        return recipe.isCompleted() ? RECIPE_COMPLETED : RECIPE_INCOMPLETED;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.MOVED_PERMANENTLY)
    @AuthRequired
    public String create(User owner, @Valid RecipeDTO dto) {
        Recipe recipe = recipeService.create(owner, dto);
        return "redirect:/recipes/" + recipe.getId();
    }
}
