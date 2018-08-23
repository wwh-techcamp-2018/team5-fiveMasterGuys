package com.woowahan.techcamp.recipehub.step.controller;

import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.step.repository.StepRepository;
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

public class StepRestAcceptanceTest extends AcceptanceTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private StepRepository stepRepository;

    private Recipe recipe;
    private StepCreationDTO.StepCreationDTOBuilder dtoBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        recipe = recipeRepository.save(Recipe.builder().name("a recipe").owner(savedUser).build());
        dtoBuilder = StepCreationDTO.builder()
                .name("recipe-step1")
                .content(Arrays.asList("a", "b"))
                .imgUrl("/static/img/image.jpg")
                .ingredients(null)
                .previousStepId(null);
    }

    @Test
    public void createFirstStep() {
        StepCreationDTO dto = dtoBuilder.build();
        ResponseEntity<RestResponse<Step>> response = requestJson("/api/recipes/" + recipe.getId() + "/steps", HttpMethod.POST,
                dto, basicAuthUser, new ParameterizedTypeReference<RestResponse<Step>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getData())
                .isEqualToComparingOnlyGivenFields(dto, "name", "content", "ingredients", "imgUrl");

        assertThat(response.getBody().getData().getSequence()).isEqualTo(1L);
    }

    @Test
    public void hideWriterPassword() {
        StepCreationDTO dto = dtoBuilder.build();
        ResponseEntity<RestResponse<Step>> response = requestJson("/api/recipes/" + recipe.getId() + "/steps", HttpMethod.POST,
                dto, basicAuthUser, new ParameterizedTypeReference<RestResponse<Step>>() {
                });

        assertThat(response.getBody().getData().getWriter().getPassword()).isNull();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        stepRepository.deleteAll();
        recipeRepository.deleteAll();
        super.tearDown();
    }
}