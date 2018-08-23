package com.woowahan.techcamp.recipehub.home.controller;

import com.woowahan.techcamp.recipehub.common.dto.PageListDTO;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private static final int ONE_BASED_DEFAULT_PAGE = 1;

    public static final int DEFAULT_PAGE_CONTENT_SIZE = 9;
    private static final int SHOWING_PAGE_SIZE = 5;

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public String home(Model model,
                       @PageableDefault(size = DEFAULT_PAGE_CONTENT_SIZE, page = ONE_BASED_DEFAULT_PAGE) Pageable pageable) {
        Page<Recipe> recipePage = recipeService.findAllByPagable(zeroBased(pageable));

        model.addAttribute("recipes", recipePage.getContent());
        model.addAttribute("paginationList", PageListDTO.from(recipePage, SHOWING_PAGE_SIZE));

        return "index";
    }

    private static Pageable zeroBased(Pageable pageable) {
        return pageable.previousOrFirst();
    }
}
