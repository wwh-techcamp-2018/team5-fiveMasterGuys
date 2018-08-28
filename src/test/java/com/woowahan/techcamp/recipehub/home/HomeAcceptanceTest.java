package com.woowahan.techcamp.recipehub.home;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeAcceptanceTest extends AcceptanceTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void mainPage() throws Exception {
        assertThat(requestGet("/").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void mainPageWithRecipes() throws Exception {
        recipeRepository.save(Recipe.builder()
                .name("Hello")
                .category(categoryRepository.save(new Category("a")))
                .owner(savedUser)
                .build());
        ResponseEntity<String> response = requestGet("/");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Hello");
    }

    @Override
    @After
    public void tearDown() throws Exception {
        recipeRepository.deleteAll();
        super.tearDown();
    }
}
