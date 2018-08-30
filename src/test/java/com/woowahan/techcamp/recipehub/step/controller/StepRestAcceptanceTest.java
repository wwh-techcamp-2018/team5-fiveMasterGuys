package com.woowahan.techcamp.recipehub.step.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
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

    @Autowired
    private CategoryRepository categoryRepository;

    private Step firstStep;
    private StepOffer stepAppendOffer;
    private Recipe recipe;

    private StepCreationDTO.StepCreationDTOBuilder creationDtoBuilder;
    private StepOffer.StepOfferBuilder stepOfferBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        recipe = recipeRepository.save(Recipe.builder()
                .name("a recipe")
                .owner(savedRecipeOwner)
                .imgUrl("/static/img/image.jpg")
                .recipeSteps(null)
                .category(categoryRepository.save(Category.builder().title("category").build()))
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


        stepOfferBuilder = StepOffer.builder()
                .writer(savedUser)
                .offerType(OfferType.MODIFY)
                .content(Arrays.asList("Boil", "Cut", "Cook"))
                .name("Eat")
                .recipe(recipe)
                .target(firstStep)
                .ingredients(null)
                .rejected(false)
                .imgUrl("/static/img/image.jpg");

    }

    @Test
    public void createFirstStep() {
        StepCreationDTO dto = creationDtoBuilder.targetStepId(null).build();
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
        StepCreationDTO dto = creationDtoBuilder.targetStepId(firstStep.getId()).build();
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
        StepOffer otherOffer = stepOfferRepository.save(StepOffer.builder()
                .name("recipe-step1-other-offer")
                .writer(savedUser)
                .content(Arrays.asList("Step", "Append", "Other", "Offer"))
                .imgUrl("/static/img/fixed-image.jpg")
                .recipe(recipe)
                .offerType(OfferType.APPEND)
                .target(firstStep)
                .rejected(false)
                .ingredients(null)
                .build());

        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + otherOffer.getId() + "/approve",
                HttpMethod.GET,
                basicAuthRecipeOwner,
                stepType());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(stepOfferRepository.findById(stepAppendOffer.getId()).get().isRejected())
                .isTrue();

        assertThat(response.getBody().getData()).isInstanceOf(Step.class);
        assertThat(response.getBody().getData().getId())
                .isEqualTo(stepRepository.findById(otherOffer.getId()).get().getId());

        assertThat(response.getBody().getData())
                .isEqualToComparingOnlyGivenFields(otherOffer, "name", "writer", "content", "imgUrl");

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
    public void approveOfferByClosedTarget() {
        Step closedStep = stepRepository.save(Step.builder()
                .name("recipe-closed-step")
                .writer(savedRecipeOwner)
                .content(Arrays.asList("Closed", "Step"))
                .imgUrl("/static/img/image.jpg")
                .offers(null)
                .recipe(recipe)
                .sequence(1L)
                .closed(true)
                .ingredients(null)
                .build());

        StepOffer offer = stepOfferBuilder.target(closedStep).build();

        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + offer.getId() + "/approve",
                HttpMethod.GET,
                basicAuthRecipeOwner,
                stepType());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getStep() throws Exception {
        Step savedStep = stepRepository.save(
                Step.builder()
                        .recipe(recipe)
                        .writer(savedUser)
                        .sequence(1L)
                        .closed(false)
                        .imgUrl("")
                        .content(new ArrayList<>())
                        .name("test step")
                        .build());

        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + savedStep.getId(), HttpMethod.GET, stepType());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody().getData().getId()).isEqualTo(savedStep.getId());
        assertThat(response.getBody().getData().getWriter()).isEqualTo(savedStep.getWriter());
        assertThat(response.getBody().getData().getName()).isEqualTo(savedStep.getName());
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
        StepCreationDTO dto = creationDtoBuilder.targetStepId(firstStep.getId()).build();

        StepOffer appendOffer = stepOfferRepository.save(
                StepOffer.from(savedUser, dto, recipe, firstStep, OfferType.APPEND)
        );

        StepOffer modifyOffer = stepOfferRepository.save(
                StepOffer.from(savedUser, dto, recipe, firstStep, OfferType.MODIFY)
        );

        //when
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + firstStep.getId(),
                HttpMethod.PUT,
                dto,
                basicAuthRecipeOwner,
                stepType());

        //then
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
                .ingredients(null).targetStepId(targetStep.getId()).content(content).build();

        //then
        ResponseEntity<RestResponse<Step>> request = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + targetStep.getId(),
                HttpMethod.PUT, dto, contributor, stepType());
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.OK);

        StepOffer offer = stepOfferRepository.findById(request.getBody().getData().getId()).get();
        assertThat(offer.getTarget()).isEqualTo(targetStep);
        assertThat(offer.getOfferType()).isEqualTo(OfferType.MODIFY);
    }

    @Test
    public void modifyOfferToClosedStep() throws Exception {

        //given
        Step targetStep = stepRepository.save(
                Step.builder()
                        .recipe(recipe)
                        .writer(savedUser)
                        .sequence(1L)
                        .closed(true)
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
                .ingredients(null).targetStepId(targetStep.getId()).content(content).build();

        //then
        ResponseEntity<RestResponse<Step>> request = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + targetStep.getId(),
                HttpMethod.PUT, dto, contributor, stepType());
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void modifyByNotLoginedUser() {
        StepCreationDTO dto = creationDtoBuilder.targetStepId(firstStep.getId()).build();
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

    @Test
    public void approveStepModifyOfferByOwner() {
        // Given
        StepOffer willBeStepModifyOffer = stepOfferRepository.save(stepOfferBuilder.build());
        StepOffer modifyOffer = stepOfferRepository.save(stepOfferBuilder.content(Arrays.asList("a", "b", "c")).build());

        // When
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + willBeStepModifyOffer.getId() + "/approve",
                HttpMethod.GET,
                basicAuthRecipeOwner,
                stepType());

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData()).isInstanceOf(Step.class);

        assertThat(stepOfferRepository.findById(modifyOffer.getId()).get().isRejected()).isTrue();
        assertThat(response.getBody().getData())
                .isEqualToComparingOnlyGivenFields(willBeStepModifyOffer, "name", "writer", "content", "imgUrl");
        assertThat(response.getBody().getData().getOffers()).isNull();
    }

    @Test
    public void approveModifyOfferByOwnerWithStepAppendOffer() {
        // Given
        StepOffer willBeStepModifyOffer = stepOfferRepository.save(stepOfferBuilder.build());
        StepOffer modifyOffer = stepOfferRepository.save(stepOfferBuilder.content(Arrays.asList("a", "b", "c")).build());

        // When
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + willBeStepModifyOffer.getId() + "/approve",
                HttpMethod.GET,
                basicAuthRecipeOwner,
                stepType());

        Step resultStep = response.getBody().getData();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultStep).isInstanceOf(Step.class);

        assertThat(stepOfferRepository.findById(modifyOffer.getId()).get().isRejected()).isTrue();
        assertThat(resultStep)
                .isEqualToComparingOnlyGivenFields(willBeStepModifyOffer, "name", "writer", "content", "imgUrl");

        assertThat(stepOfferRepository.findById(stepAppendOffer.getId()).get().getTarget()).isEqualTo(resultStep);
    }

    @Test
    public void notApproveStepModifyOfferByNotLoginUser() {
        // Given
        StepOffer willBeStepModifyOffer = stepOfferRepository.save(stepOfferBuilder.build());
        StepOffer modifyOffer = stepOfferRepository.save(stepOfferBuilder.content(Arrays.asList("a", "b", "c")).build());

        // When
        ResponseEntity<RestResponse<Step>> response = requestJson(
                "/api/recipes/" + recipe.getId() + "/steps/" + willBeStepModifyOffer.getId() + "/approve",
                HttpMethod.GET,
                stepType());

        Step originalFirstStep = stepRepository.findById(firstStep.getId()).get();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(firstStep).isEqualToIgnoringGivenFields(originalFirstStep, "ingredients", "offers");
        assertThat(willBeStepModifyOffer.getOfferType()).isEqualTo(OfferType.MODIFY);
        assertThat(modifyOffer.getOfferType()).isEqualTo(OfferType.MODIFY);
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