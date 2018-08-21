package com.woowahan.techcamp.recipehub.recipestep.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepRequestDTO;
import com.woowahan.techcamp.recipehub.recipestep.service.RecipeStepRepository;
import com.woowahan.techcamp.recipehub.recipestep.service.RecipeStepRequestRepository;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.domain.UserRepository;
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

public class RecipeStepAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Autowired
    private RecipeStepRequestRepository recipeStepRequestRepository;

    private User otherUser;
    private Recipe recipe;
    private RecipeStep step;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        Category category = categoryRepository.save(new Category("pizza"));

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

        step = recipeStepRepository.save(RecipeStep.builder().writer(savedUser).name("Put Basil").ingredients(null)
                .content(Arrays.asList("토마토 페이스트를 딴다", "적당량을 붓는다", "얇게 펴준다")).build());
    }

    @Test
    public void create() {
        List<String> content = Arrays.asList("토마토 페이스트를 딴다", "적당량을 붓는다", "얇게 펴준다");
        RecipeStepCreationDTO dto = RecipeStepCreationDTO.builder().name("Put tomato paste")
                .ingredients(null).previousStepId(step.getId()).content(content).recipeId(recipe.getId()).build();

        ResponseEntity<RestResponse<RecipeStepRequestDTO>> request = requestJson("/api/recipes/" + recipe.getId() + "/steps", HttpMethod.POST, dto, otherUser, getRecipeStepRequestTypeRef());
        assertThat(request.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private ParameterizedTypeReference<RestResponse<RecipeStepRequestDTO>> getRecipeStepRequestTypeRef() {
        return new ParameterizedTypeReference<RestResponse<RecipeStepRequestDTO>>() {
        };
    }

    @Override
    @After
    public void tearDown() throws Exception {

        recipeStepRequestRepository.deleteAll();
        recipeStepRepository.deleteAll();
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        super.tearDown();
    }
}
