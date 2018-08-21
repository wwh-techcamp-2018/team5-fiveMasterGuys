package com.woowahan.techcamp.recipehub.recipestep.controller;

import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStepRepository;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeStepRestAcceptanceTest extends AcceptanceTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    private Recipe recipe;
    private RecipeStepCreationDTO.RecipeStepCreationDTOBuilder dtoBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        recipe = recipeRepository.save(Recipe.builder().name("a recipe").owner(savedUser).build());
        dtoBuilder = RecipeStepCreationDTO.builder()
                .name("recipe-step1")
                .content(Arrays.asList("a", "b"))
                .imgUrl("/static/img/image.jpg")
                .ingredients(null)
                .previousStepId(null);
    }

    @Test
    public void createFirstStep() {
        RecipeStepCreationDTO dto = dtoBuilder.build();
        ResponseEntity<RestResponse<RecipeStep>> response = requestJson("/api/recipes/" + recipe.getId() + "/steps", HttpMethod.POST,
                dto, basicAuthUser, new ParameterizedTypeReference<RestResponse<RecipeStep>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getData())
                .isEqualToComparingOnlyGivenFields(dto, "name", "content", "ingredients", "imgUrl");

        assertThat(response.getBody().getData().getSequence()).isEqualTo(1L);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        recipeStepRepository.deleteAll();
        recipeRepository.deleteAll();
        super.tearDown();
    }
}