package com.woowahan.techcamp.recipehub.recipestep.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStepRepository;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class OwnerRecipeStepServiceTest {

    @InjectMocks
    private OwnerRecipeStepService service;

    @Mock
    private RecipeStepRepository recipeStepRepository;
    private RecipeStepCreationDTO.RecipeStepCreationDTOBuilder dtoBuilder;
    private RecipeStep recipeStep;

    @Before
    public void setUp() throws Exception {

        dtoBuilder = RecipeStepCreationDTO.builder()
                .name("asdf")
                .content(Arrays.asList("a", "b"))
                .ingredients(null)
                .previousStepId(null)
                .imgUrl("/static/img/image.jpg");
    }

    @Test
    public void createFirstStep() {
        User owner = User.builder().id(1L).build();
        Recipe recipe = Recipe.builder().build();

        RecipeStepCreationDTO dto = dtoBuilder.build();

        when(recipeStepRepository.save(any())).then(returnsFirstArg());

        RecipeStep resultRecipeStep = service.create(
                owner,
                dto,
                recipe);


        verify(recipeStepRepository).increaseSequenceGte(recipe, 1L);
        assertThat(dto).isEqualToComparingOnlyGivenFields(resultRecipeStep, "name", "content", "ingredients", "imgUrl");

        assertThat(resultRecipeStep.getRecipe()).isEqualTo(recipe);
        assertThat(resultRecipeStep.getWriter()).isEqualTo(owner);
        assertThat(resultRecipeStep.getSequence()).isEqualTo(1L);
        assertThat(resultRecipeStep.isClosed()).isFalse();
    }

    @Test
    public void createWithPreviousStep() {
        Long previousStepId = 1L;
        RecipeStepCreationDTO dto = dtoBuilder.previousStepId(previousStepId).build();

        RecipeStep previousStep = RecipeStep.builder().id(previousStepId).sequence(3L).build();

        User owner = User.builder().id(1L).build();
        Recipe recipe = Recipe.builder().build();

        when(recipeStepRepository.findById(previousStepId)).thenReturn(Optional.of(previousStep));

        when(recipeStepRepository.save(any())).then(returnsFirstArg());
        RecipeStep resultRecipeStep = service.create(
                owner,
                dto,
                recipe);

        verify(recipeStepRepository).increaseSequenceGte(recipe, previousStep.getSequence() + 1);

        assertThat(dto).isEqualToComparingOnlyGivenFields(resultRecipeStep, "name", "content", "ingredients", "imgUrl");

        assertThat(resultRecipeStep.getRecipe()).isEqualTo(recipe);
        assertThat(resultRecipeStep.getWriter()).isEqualTo(owner);
        assertThat(resultRecipeStep.getSequence()).isEqualTo(previousStep.getSequence() + 1);
        assertThat(resultRecipeStep.isClosed()).isFalse();
    }
}