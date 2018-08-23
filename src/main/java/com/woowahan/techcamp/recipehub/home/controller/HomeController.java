package com.woowahan.techcamp.recipehub.home.controller;

import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    public static final int DEFAULT_PAGE_SIZE = 9;

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public String home(Model model, @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        model.addAttribute("recipes", recipeService.findAllByPagable(pageable));
        return "index";
    }
}
