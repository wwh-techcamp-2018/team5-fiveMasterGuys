package com.woowahan.techcamp.recipehub.home.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.service.CategoryService;
import com.woowahan.techcamp.recipehub.common.dto.PageListDTO;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private static final int ONE_BASED_DEFAULT_PAGE = 1;

    public static final int DEFAULT_PAGE_CONTENT_SIZE = 9;
    private static final int SHOWING_PAGE_SIZE = 5;

    private static final String SORT_BY_CREATED_AT = "createdAt";

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String home(Model model,
                       @PageableDefault(size = DEFAULT_PAGE_CONTENT_SIZE, page = ONE_BASED_DEFAULT_PAGE, sort = SORT_BY_CREATED_AT, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Recipe> recipePage = recipeService.findAllByPageable(zeroBased(pageable));

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        addAttributes(model, recipePage, "/?");

        return "index";
    }

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(name = "category", required = false) Long categoryId,
                         @RequestParam(name = "q", defaultValue = "") String keyword,
                         @PageableDefault(size = DEFAULT_PAGE_CONTENT_SIZE, page = ONE_BASED_DEFAULT_PAGE, sort = SORT_BY_CREATED_AT, direction = Sort.Direction.DESC) Pageable pageable) {

        if (categoryId == null) {
            addAttributes(model, recipeService.search(keyword, zeroBased(pageable)), buildBaseUrl(keyword));
            return "index";
        }
        addAttributes(model, recipeService.search(categoryId, keyword, zeroBased(pageable)), buildBaseUrl(keyword, categoryId));
        return "index";
    }

    private String buildBaseUrl(String keyword) {
        return String.format("/search?q=%s&", keyword);
    }

    private String buildBaseUrl(String keyword, Long categoryId) {
        return String.format("/search?q=%s&category=%d&", keyword, categoryId);
    }

    private void addAttributes(Model model, Page<Recipe> recipePage, String baseUrl) {
        model.addAttribute("recipes", recipePage.getContent());
        model.addAttribute("paginationList", PageListDTO.from(recipePage, SHOWING_PAGE_SIZE));
        model.addAttribute("baseUrl", baseUrl);
    }

    private static Pageable zeroBased(Pageable pageable) {
        return pageable.previousOrFirst();
    }
}
