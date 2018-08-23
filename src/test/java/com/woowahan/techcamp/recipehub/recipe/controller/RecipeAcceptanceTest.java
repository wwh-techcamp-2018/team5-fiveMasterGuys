package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeCreationDTO;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeAcceptanceTest extends AcceptanceTest {

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
    public void get() throws Exception {
        Recipe recipe = Recipe.builder().name("초코치킨").category(category).owner(savedUser).build();
        Recipe saved = recipeRepository.save(recipe);
        ResponseEntity<String> resp = requestGet("/recipes/" + saved.getId());
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("초코치킨");
    }

    @Test
    public void getWrongRecipeId() throws Exception {
        ResponseEntity<String> resp = requestGet("/recipes/" + Long.MAX_VALUE);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).contains("404. Not Found.");
    }

    @Test
    public void create() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().categoryId(category.getId()).name("초코치킨").build();
        ResponseEntity<String> resp = requestPost("/recipes", dto, basicAuthUser);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
    }

    @Test
    public void createWrongCategoryId() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().categoryId(Long.MAX_VALUE).name("초코치킨").build();
        ResponseEntity<String> resp = requestPost("/recipes", dto, basicAuthUser);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createWithoutName() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().categoryId(category.getId()).name(null).build();
        ResponseEntity<String> resp = requestPost("/recipes", dto, basicAuthUser);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createWithoutDTO() {
        ResponseEntity<String> resp = requestPost("/recipes", null, basicAuthUser);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createNotLogin() {
        RecipeCreationDTO dto = RecipeCreationDTO.builder().categoryId(category.getId()).name("초코치킨").build();
        ResponseEntity<String> resp = requestPost("/recipes", dto, null);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void createPageNotLogin() {
        ResponseEntity<String> resp = requestGet("/recipes/create", null);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void createPageWithLogin() {
        ResponseEntity<String> resp = requestGet("/recipes/create", basicAuthUser);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        super.tearDown();
    }
}
