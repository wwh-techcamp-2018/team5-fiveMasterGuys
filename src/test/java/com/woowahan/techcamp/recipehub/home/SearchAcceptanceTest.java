package com.woowahan.techcamp.recipehub.home;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import com.woowahan.techcamp.recipehub.support.util.FixtureFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchAcceptanceTest extends AcceptanceTest {
    private static final String RECIPE_NAME = "테스트 레시피입니다.";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private Category category;


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        category = categoryRepository.save(new Category("category"));

        recipeRepository.saveAll(FixtureFactory.generateRecipeList(category, 10));
        recipeRepository.save(Recipe.builder().name(RECIPE_NAME).category(category).build());
    }

    @Test
    public void searchByNotExistingCategory() {
        String url = buildSearchUrl(RECIPE_NAME, Long.MAX_VALUE);
        ResponseEntity<String> response = template().getForEntity(url, String.class);
        assertThat(response.getBody()).containsIgnoringCase("404. Not Found.");
    }

    @Test
    public void searchByInvalidCategory() {
        String url = buildSearchUrl(RECIPE_NAME, "some kind of string");
        ResponseEntity<String> response = template().getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void search() {
        String url = buildSearchUrl(RECIPE_NAME, category.getId());
        ResponseEntity<String> response = template().getForEntity(url, String.class);
        assertThat(response.getBody()).doesNotContain("404. Not Found.");
        assertThat(response.getBody()).containsIgnoringCase(RECIPE_NAME);
    }

    @Test
    public void searchResultsAreMoreThan2Pages() {
        String url = buildSearchUrl(FixtureFactory.RECIPE_NAME_PREFIX, category.getId());
        ResponseEntity<String> response = template().getForEntity(url, String.class);
        assertThat(response.getBody()).containsPattern(FixtureFactory.RECIPE_NAME_PREFIX + ".*&amp;page=2");
    }

    private String buildSearchUrl(String keyword, Object categoryId) {
        return "/search?q=" + keyword + "&category=" + categoryId;
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
