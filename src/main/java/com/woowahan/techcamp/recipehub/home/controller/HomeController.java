package com.woowahan.techcamp.recipehub.home.controller;

import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("recipes", recipeService.findAll());
        return "index";
    }
}
