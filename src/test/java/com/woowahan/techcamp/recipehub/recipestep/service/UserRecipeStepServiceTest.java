package com.woowahan.techcamp.recipehub.recipestep.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStepRequest;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStepRequestRepository;
import com.woowahan.techcamp.recipehub.recipestep.domain.RequestType;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRecipeStepServiceTest {

    @InjectMocks
    private UserRecipeStepService userRecipeStepService;

    @Mock
    private RecipeStepRequestRepository recipeStepRequestRepository;

    private User user;
    private Recipe recipe;

    @Before
    public void setUp() throws Exception {
        user = User.builder().build();
        recipe = Recipe.builder().build();
    }

    @Test
    public void createNullTarget() {
        // given
        String name = "핏짜";
        RecipeStepCreationDTO dto = RecipeStepCreationDTO.builder()
                .name(name).build();

        RecipeStepRequest saved = RecipeStepRequest.from(user, dto, recipe, null, RequestType.APPEND);
        when(recipeStepRequestRepository.save(saved)).thenReturn(saved);

        // when
        RecipeStepRequest stepRequest = userRecipeStepService.create(user, dto, recipe);

        // then
        assertThat(stepRequest.getTarget()).isNull();
        assertThat(stepRequest.getName()).isEqualTo(name);
        assertThat(stepRequest.getRequestType()).isEqualTo(RequestType.APPEND);
    }


    @Test
    public void create() {
        // given
        String name = "핏짜";

        RecipeStep targetStep = RecipeStep.builder().build();
        RecipeStepCreationDTO dto = RecipeStepCreationDTO.builder()
                .name(name).build();

        RecipeStepRequest saved = RecipeStepRequest.from(user, dto, recipe, targetStep, RequestType.APPEND);
        when(recipeStepRequestRepository.save(saved)).thenReturn(saved);

        // when
        RecipeStepRequest stepRequest = userRecipeStepService.create(user, dto, recipe);

        // then
        assertThat(stepRequest.getName()).isEqualTo(name);
        assertThat(stepRequest.getTarget()).isEqualTo(targetStep);
        assertThat(stepRequest.getRequestType()).isEqualTo(RequestType.APPEND);
    }

}
