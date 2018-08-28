package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeDTO;
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

    private Recipe defaultRecipe;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        category = categoryRepository.save(Category.builder().title("치킨").build());
        defaultRecipe = recipeRepository.save(Recipe.builder().owner(savedRecipeOwner).name("Default Recipe").category(category).build());
    }

    @Test
    public void create() {
        RecipeDTO dto = RecipeDTO.builder().categoryId(category.getId()).name("초코치킨").build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson("/api/recipes", HttpMethod.POST, dto, basicAuthRecipeOwner,
                new ParameterizedTypeReference<RestResponse<Recipe>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createWrongDTO() {
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson("/api/recipes", HttpMethod.POST, dto, basicAuthRecipeOwner,
                new ParameterizedTypeReference<RestResponse<Recipe>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createWithUnauthorized() {
        RecipeDTO dto = RecipeDTO.builder().name("초코치킨").build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson("/api/recipes", HttpMethod.POST, dto,
                new ParameterizedTypeReference<RestResponse<Recipe>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void complete() {
        final long recipeId = defaultRecipe.getId();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson("/api/recipes/" + recipeId + "/complete", HttpMethod.POST, null, basicAuthRecipeOwner,
                new ParameterizedTypeReference<RestResponse<Recipe>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody().getData().isCompleted()).isTrue();
    }

    @Test
    public void completeWithNoAuthority() {
        final long recipeId = defaultRecipe.getId();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson("/api/recipes/" + recipeId + "/complete", HttpMethod.POST, null, basicAuthUser,
                new ParameterizedTypeReference<RestResponse<Recipe>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void completeWithUnauthenticated() {
        final long recipeId = defaultRecipe.getId();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson("/api/recipes/" + recipeId + "/complete", HttpMethod.POST,
                new ParameterizedTypeReference<RestResponse<Recipe>>() {
                });
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getIncompletedRecipePageOwner() {
        final long recipeId = defaultRecipe.getId();
        ResponseEntity<String> response = template(basicAuthRecipeOwner).getForEntity("/recipes/" + recipeId, String.class);
        assertThat(response.getBody()).contains("레시피 완성하기");
    }

    @Test
    public void getIncompletedRecipePageNotOwner() {
        final long recipeId = defaultRecipe.getId();
        ResponseEntity<String> response = template(basicAuthUser).getForEntity("/recipes/" + recipeId, String.class);
        assertThat(response.getBody()).doesNotContain("레시피 완성하기");
    }

    @Test
    public void getIncompletedRecipePageNotLoggedIn() {
        final long recipeId = defaultRecipe.getId();
        ResponseEntity<String> response = template().getForEntity("/recipes/" + recipeId, String.class);
        assertThat(response.getBody()).doesNotContain("레시피 완성하기");
    }

    @Test
    public void modifyRecipeImage() {
        String newImageUrl = "http://s3.aws.image.com/newimage.png";


        RecipeDTO dto = RecipeDTO.builder().imgUrl(newImageUrl).build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson(
                "/api/recipes/" + defaultRecipe.getId(),
                HttpMethod.PUT,
                dto,
                basicAuthRecipeOwner,
                recipeType());

        Recipe modified = recipeRepository.findById(defaultRecipe.getId()).get();

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(modified.getImgUrl()).isEqualTo(newImageUrl);
        assertThat(modified).isEqualToComparingOnlyGivenFields(
                defaultRecipe,
                "name",
                "owner");
        assertThat(modified.getCategory().getTitle()).isEqualTo(defaultRecipe.getCategory().getTitle());
    }

    @Test
    public void modifyRecipeImageByNotOwner() {
        String newImageUrl = "http://s3.aws.image.com/newimage.png";

        RecipeDTO dto = RecipeDTO.builder().imgUrl(newImageUrl).build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson(
                "/api/recipes/" + defaultRecipe.getId(),
                HttpMethod.PUT,
                dto,
                basicAuthUser,
                recipeType());
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void modifyRecipeImageByNotLoginedUser() {
        String newImageUrl = "http://s3.aws.image.com/newimage.png";

        RecipeDTO dto = RecipeDTO.builder().imgUrl(newImageUrl).build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson(
                "/api/recipes/" + defaultRecipe.getId(),
                HttpMethod.PUT,
                dto,
                recipeType());
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void modifyNotExistRecipe() {
        String newImageUrl = "http://s3.aws.image.com/newimage.png";

        RecipeDTO dto = RecipeDTO.builder().imgUrl(newImageUrl).build();
        ResponseEntity<RestResponse<Recipe>> resp = requestJson(
                "/api/recipes/100",
                HttpMethod.PUT,
                dto,
                basicAuthRecipeOwner,
                recipeType());
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    public ParameterizedTypeReference<RestResponse<Recipe>> recipeType() {
        return new ParameterizedTypeReference<RestResponse<Recipe>>() {
        };
    }

    @Override
    @After
    public void tearDown() throws Exception {
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        super.tearDown();
    }
}
