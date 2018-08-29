package com.woowahan.techcamp.recipehub.step.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StepAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private StepOfferRepository stepOfferRepository;

    private User otherUser;
    private Recipe recipe;
    private Step step;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        Category category = categoryRepository.save(Category.builder().title("pizza").build());

        User.UserBuilder userBuilder = User.builder()
                .email("other@other.com")
                .name("Other")
                .password("asdfqwer1234");

        otherUser = userBuilder.build();

        recipe = recipeRepository.save(
                Recipe.builder()
                        .name("recipe")
                        .imgUrl("img")
                        .category(category)
                        .owner(savedUser)
                        .build());

        userRepository.save(userBuilder
                .password(new BCryptPasswordEncoder().encode("asdfqwer1234"))
                .build());

        step = stepRepository.save(Step.builder().recipe(recipe).writer(savedUser).name("Put Basil").ingredients(null)
                .content(Arrays.asList("토마토 페이스트를 딴다", "적당량을 붓는다", "얇게 펴준다")).build());
    }

    @Test
    public void createStepOfferAppend() {
        List<String> content = Arrays.asList("토마토 페이스트를 딴다", "적당량을 붓는다", "얇게 펴준다");
        StepCreationDTO dto = StepCreationDTO.builder().name("Put tomato paste")
                .ingredients(null).targetStepId(step.getId()).content(content).build();

        ResponseEntity<RestResponse<StepOffer>> request = requestJson("/api/recipes/" + recipe.getId() + "/steps", HttpMethod.POST, dto, otherUser, getRecipeStepRequestTypeRef());
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createStepOfferAppendWithNullDTO() {
        List<String> content = Arrays.asList("토마토 페이스트를 딴다", "적당량을 붓는다", "얇게 펴준다");
        StepCreationDTO dto = StepCreationDTO.builder().name("Put tomato paste")
                .ingredients(null).targetStepId(null).content(content).build();

        ResponseEntity<RestResponse<StepOffer>> request = requestJson("/api/recipes/" + recipe.getId() + "/steps", HttpMethod.POST, dto, otherUser, getRecipeStepRequestTypeRef());
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private ParameterizedTypeReference<RestResponse<StepOffer>> getRecipeStepRequestTypeRef() {
        return new ParameterizedTypeReference<RestResponse<StepOffer>>() {
        };
    }

    @Override
    @After
    public void tearDown() throws Exception {

        stepOfferRepository.deleteAll();
        stepRepository.deleteAll();
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        super.tearDown();
    }
}
