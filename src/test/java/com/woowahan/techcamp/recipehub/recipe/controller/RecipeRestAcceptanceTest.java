package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeCreationDTO;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeRestAcceptanceTest extends AcceptanceTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    private Category category;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        category = categoryRepository.save(Category.builder().title("치킨").build());
    }

    @Test
    public void create() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().categoryId(category.getId()).name("초코치킨").build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson("/api/recipes", HttpMethod.POST, dto, basicAuthUser,
                new ParameterizedTypeReference<RestResponse<Recipe>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createWrongDTO() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().name("초코치킨").build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson("/api/recipes", HttpMethod.POST, dto, basicAuthUser,
                new ParameterizedTypeReference<RestResponse<Recipe>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Override
    @After
    public void tearDown() throws Exception {
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        super.tearDown();
    }
}
