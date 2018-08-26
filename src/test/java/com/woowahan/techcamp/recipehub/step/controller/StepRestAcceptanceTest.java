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
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class StepRestAcceptanceTest extends AcceptanceTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StepOfferRepository stepOfferRepository;

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
                dto, basicAuthUser, stepType());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody().getData())
                .isEqualToComparingOnlyGivenFields(dto, "name", "content", "ingredients", "imgUrl");

        assertThat(response.getBody().getData().getSequence()).isEqualTo(1L);
    }

    @Test
    public void hideWriterPassword() {
        StepCreationDTO dto = dtoBuilder.build();
        ResponseEntity<RestResponse<Step>> response = requestJson("/api/recipes/" + recipe.getId() + "/steps", HttpMethod.POST,
                dto, basicAuthUser, stepType());

        assertThat(response.getBody().getData().getWriter().getPassword()).isNull();
    }

    @Test
    public void modifyByOwner() {

        //given
        Step oldStep = stepRepository.save(
                Step.builder()
                        .recipe(recipe)
                        .writer(savedUser)
                        .sequence(1L)
                        .closed(false)
                        .imgUrl("")
                        .content(new ArrayList<>())
                        .name("test step")
                        .build());
        StepCreationDTO dto = dtoBuilder.previousStepId(oldStep.getId()).build();
        StepOffer appendOffer = stepOfferRepository.save(
                StepOffer.from(savedUser, dto, recipe, oldStep, OfferType.APPEND)
        );
        StepOffer modifyOffer = stepOfferRepository.save(
                StepOffer.from(savedUser, dto, recipe, oldStep, OfferType.MODIFY)
        );

        //when
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + oldStep.getId(),
                HttpMethod.PUT,
                dto, basicAuthUser,
                stepType());

        //then
        Step responseStep = response.getBody().getData();
        Step savedStep = stepRepository.findById(responseStep.getId()).get();
        oldStep = stepRepository.findById(oldStep.getId()).get();
        appendOffer = stepOfferRepository.findById(appendOffer.getId()).get();
        modifyOffer = stepOfferRepository.findById(modifyOffer.getId()).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StepCreationDTOTest.assertDtoEqualToStep(dto, responseStep);
        StepCreationDTOTest.assertDtoEqualToStep(dto, savedStep);
        assertThat(appendOffer.getTarget()).isEqualTo(savedStep);
        assertThat(savedStep.isClosed()).isFalse();
        assertThat(modifyOffer.getTarget()).isEqualTo(oldStep);
        assertThat(oldStep.isClosed()).isTrue();
        assertThat(modifyOffer.isRejected()).isTrue();
    }

    @Test
    public void modifyByContributor() throws Exception {

        //given
        Step targetStep = stepRepository.save(
                Step.builder()
                        .recipe(recipe)
                        .writer(savedUser)
                        .sequence(1L)
                        .closed(false)
                        .imgUrl("")
                        .content(new ArrayList<>())
                        .name("test step")
                        .build());

        User.UserBuilder userBuilder = User.builder()
                .email("other@other.com")
                .name("Other")
                .password("asdfqwer1234");

        User contributor = userBuilder.build();

        userRepository.save(userBuilder
                .password(new BCryptPasswordEncoder().encode("asdfqwer1234"))
                .build());


        //when
        List<String> content = Arrays.asList("토마토 페이스트를 딴다", "적당량을 붓는다", "얇게 펴준다");
        StepCreationDTO dto = StepCreationDTO.builder().name("Put tomato paste")
                .ingredients(null).previousStepId(targetStep.getId()).content(content).build();

        //then
        ResponseEntity<RestResponse<Step>> request = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + targetStep.getId(),
                HttpMethod.PUT, dto, contributor, stepType());
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);

        StepOffer offer = recipeRepository.findById(recipe.getId()).get().getRecipeSteps().get(0).getOffers().get(0);
        assertThat(offer.getTarget()).isEqualTo(targetStep);
        assertThat(offer.getOfferType()).isEqualTo(OfferType.MODIFY);
    }

    @Test
    public void modifyByNotLoginedUser() {
        Step oldStep = stepRepository.save(
                Step.builder()
                        .recipe(recipe)
                        .writer(savedUser)
                        .sequence(1L)
                        .closed(false)
                        .imgUrl("")
                        .content(new ArrayList<>())
                        .name("test step")
                        .build());

        StepCreationDTO dto = dtoBuilder.previousStepId(oldStep.getId()).build();
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + oldStep.getId(),
                HttpMethod.PUT,
                dto,
                stepType());

        Step oldStepAfterRequest = stepRepository.findById(oldStep.getId()).get();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(oldStep).isEqualToIgnoringGivenFields(oldStepAfterRequest, "ingredients", "offers");
        assertThat(oldStep.isClosed()).isFalse();
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