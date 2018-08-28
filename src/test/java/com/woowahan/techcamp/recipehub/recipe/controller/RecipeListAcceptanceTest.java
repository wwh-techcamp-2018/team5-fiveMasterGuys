package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.home.controller.HomeController;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.woowahan.techcamp.recipehub.support.util.FixtureFactory.generateRecipeList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeListAcceptanceTest extends AcceptanceTest {

    private static final String REQUEST_PARAM_PAGE_NUMBER = "page";
    private static final String REQUEST_PARAM_PAGE_SIZE = "size";
    private static final String REQUEST_PARAM_PAGE_SORT = "sort";

    private static final int FIRST_PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = HomeController.DEFAULT_PAGE_CONTENT_SIZE;
    private static final String SORT_ASC_ORDER_BY_NAME = "name,asc";
    private static final String SORT_DESC_ORDER_BY_NAME = "name,desc";

    private static final int TOTAL_PAGE = PAGE_SIZE + 1;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private List<Recipe> recipeList;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        recipeList = generateRecipeList(categoryRepository.save(new Category("category")), PAGE_SIZE * TOTAL_PAGE);
        recipeRepository.saveAll(recipeList);
    }

    @Test
    public void showRecipePageSortedAscendingOrderByName() throws Exception {
        String resultContent = mvc.perform(get("/")
                .param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(FIRST_PAGE_NUMBER))
                .param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .param(REQUEST_PARAM_PAGE_SORT, SORT_ASC_ORDER_BY_NAME)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        recipeList.sort(Comparator.comparing(Recipe::getName));
        assertThat(resultContent).contains(recipeList.subList(FIRST_PAGE_NUMBER, PAGE_SIZE).stream()
                .map(Recipe::getName)
                .collect(Collectors.toList()));
    }

    @Test
    public void showRecipePageSortedDescendingOrderByName() throws Exception {
        String resultContent = mvc.perform(get("/")
                .param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(FIRST_PAGE_NUMBER))
                .param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .param(REQUEST_PARAM_PAGE_SORT, SORT_DESC_ORDER_BY_NAME)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        recipeList.sort(Comparator.comparing(Recipe::getName).reversed());
        assertThat(resultContent).contains(recipeList.subList(FIRST_PAGE_NUMBER, PAGE_SIZE).stream()
                .map(Recipe::getName)
                .collect(Collectors.toList()));
    }

    @Test
    public void emptyRecipePageBecauseOfOutOfBound() throws Exception {
        String resultContent = mvc.perform(get("/")
                .param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(TOTAL_PAGE + 1))
                .param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(PAGE_SIZE))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(resultContent).doesNotContain(recipeList.stream()
                .map(Recipe::getName)
                .collect(Collectors.toList()));
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();

        recipeRepository.deleteAll();
    }
}
