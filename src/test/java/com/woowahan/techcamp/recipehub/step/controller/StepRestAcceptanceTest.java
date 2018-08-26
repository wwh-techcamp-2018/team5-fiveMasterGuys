package com.woowahan.techcamp.recipehub.step.controller;

import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.step.domain.OfferType;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTOTest;
import com.woowahan.techcamp.recipehub.step.repository.StepOfferRepository;
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

    @Autowired
    private StepOfferRepository stepOfferRepository;

    private Step firstStep;
    private StepOffer stepAppendOffer;
    private Recipe recipe;

    private StepCreationDTO.StepCreationDTOBuilder creationDtoBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        recipe = recipeRepository.save(Recipe.builder()
                .name("a recipe")
                .owner(savedRecipeOwner)
                .imgUrl("/static/img/image.jpg")
                .recipeSteps(null)
                .category(null)
                .completed(false)
                .build());

        firstStep = stepRepository.save(Step.builder()
                .name("recipe-step1")
                .writer(savedRecipeOwner)
                .content(Arrays.asList("First", "Step"))
                .imgUrl("/static/img/image.jpg")
                .offers(null)
                .recipe(recipe)
                .sequence(1L)
                .closed(false)
                .ingredients(null)
                .build());

        stepAppendOffer = stepOfferRepository.save(StepOffer.builder()
                .name("recipe-step1-offer")
                .writer(savedUser)
                .content(Arrays.asList("Step", "Append", "Offer"))
                .imgUrl("/static/img/fixed-image.jpg")
                .recipe(recipe)
                .offerType(OfferType.APPEND)
                .target(firstStep)
                .rejected(false)
                .ingredients(null)
                .build());

        creationDtoBuilder = StepCreationDTO.builder()
                .name("recipe-step-builder")
                .content(Arrays.asList("step", "builder"))
                .imgUrl("/static/img/image.jpg")
                .ingredients(null);
    }

    @Test
    public void createFirstStep() {
        StepCreationDTO dto = creationDtoBuilder.previousStepId(null).build();
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps",
                HttpMethod.POST,
                dto,
                basicAuthRecipeOwner,
                stepType());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getData())
                .isEqualToComparingOnlyGivenFields(dto, "name", "content", "ingredients", "imgUrl");

        assertThat(response.getBody().getData().getSequence()).isEqualTo(1L);
    }

    @Test
    public void createNextStep() {
        StepCreationDTO dto = creationDtoBuilder.previousStepId(firstStep.getId()).build();
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps",
                HttpMethod.POST,
                dto,
                basicAuthRecipeOwner,
                stepType());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getData())
                .isEqualToComparingOnlyGivenFields(dto, "name", "content", "ingredients", "imgUrl");

        assertThat(response.getBody().getData().getSequence()).isEqualTo(firstStep.getSequence() + 1L);
    }

    @Test
    public void approveStepAppendOffer() {
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + stepAppendOffer.getId() + "/approve",
                HttpMethod.GET,
                basicAuthRecipeOwner,
                stepType());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody().getData()).isInstanceOf(Step.class);
        assertThat(response.getBody().getData().getId()).isEqualTo(stepRepository.findById(stepAppendOffer.getId()).get().getId());

        assertThat(response.getBody().getData())
                .isEqualToComparingOnlyGivenFields(stepAppendOffer, "name", "writer", "content", "imgUrl");

        assertThat(response.getBody().getData().getSequence()).isEqualTo(firstStep.getSequence() + 1L);
        assertThat(response.getBody().getData().isClosed()).isFalse();
    }

    @Test
    public void approveStepAppendOfferByNotOwner() {
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + stepAppendOffer.getId() + "/approve",
                HttpMethod.GET,
                basicAuthUser,
                stepType());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void approveStepAppendOfferByNotLoginUser() {
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + stepAppendOffer.getId() + "/approve",
                HttpMethod.GET,
                stepType());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void hideWriterPassword() {
        StepCreationDTO dto = creationDtoBuilder.build();
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps",
                HttpMethod.POST,
                dto,
                basicAuthRecipeOwner,
                stepType());

        assertThat(response.getBody().getData().getWriter().getPassword()).isNull();
    }

    @Test
    public void modifyByOwner() {
        StepCreationDTO dto = creationDtoBuilder.previousStepId(firstStep.getId()).build();

        StepOffer appendOffer = stepOfferRepository.save(
                StepOffer.from(savedUser, dto, recipe, firstStep, OfferType.APPEND)
        );

        StepOffer modifyOffer = stepOfferRepository.save(
                StepOffer.from(savedUser, dto, recipe, firstStep, OfferType.MODIFY)
        );

        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + firstStep.getId(),
                HttpMethod.PUT,
                dto,
                basicAuthRecipeOwner,
                stepType());

        Step responseStep = response.getBody().getData();

        Step savedStep = stepRepository.findById(responseStep.getId()).get();

        firstStep = stepRepository.findById(firstStep.getId()).get();
        appendOffer = stepOfferRepository.findById(appendOffer.getId()).get();
        modifyOffer = stepOfferRepository.findById(modifyOffer.getId()).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StepCreationDTOTest.assertDtoEqualToStep(dto, responseStep);
        StepCreationDTOTest.assertDtoEqualToStep(dto, savedStep);
        assertThat(appendOffer.getTarget()).isEqualTo(savedStep);
        assertThat(savedStep.isClosed()).isFalse();
        assertThat(modifyOffer.getTarget()).isEqualTo(firstStep);
        assertThat(firstStep.isClosed()).isTrue();
        assertThat(modifyOffer.isRejected()).isTrue();
    }


    @Test
    public void modifyByNotLoginedUser() {
        StepCreationDTO dto = creationDtoBuilder.previousStepId(firstStep.getId()).build();
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + firstStep.getId(),
                HttpMethod.PUT,
                dto,
                stepType());

        Step oldStepAfterRequest = stepRepository.findById(firstStep.getId()).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(firstStep).isEqualToIgnoringGivenFields(oldStepAfterRequest, "ingredients", "offers");
        assertThat(firstStep.isClosed()).isFalse();
    }


    @Override
    @After
    public void tearDown() throws Exception {
        stepOfferRepository.deleteAll();
        stepRepository.deleteAll();
        recipeRepository.deleteAll();
        super.tearDown();
    }

    private ParameterizedTypeReference<RestResponse<Step>> stepType() {
        return new ParameterizedTypeReference<RestResponse<Step>>() {
        };
    }
}