package com.woowahan.techcamp.recipehub.recipe.dto;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepDTO;
import com.woowahan.techcamp.recipehub.recipestep.util.RecipeStepContentConverter;
import com.woowahan.techcamp.recipehub.recipestep.util.RecipeStepContentJsonConverter;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeResponseDTOTest {

    private RecipeStep openedStep;
    private RecipeStep closedStep;

    private List<RecipeStep> stepList;

    private RecipeStepContentConverter converter = new RecipeStepContentJsonConverter();

    @Before
    public void setUp() throws Exception {
        openedStep = RecipeStep.builder()
                .closed(false)
                .build();
        closedStep = RecipeStep.builder()
                .closed(true)
                .build();

        stepList = Arrays.asList(openedStep, closedStep);
    }

    @Test
    public void fromCompletedRecipe() {
        Recipe completedRecipe = Recipe.builder().completed(true).recipeSteps(stepList).build();
        RecipeResponseDTO detailDTO = RecipeResponseDTO.from(completedRecipe, converter);

        assertThat(detailDTO.getRecipeSteps()).containsExactly(RecipeStepDTO.from(openedStep, converter));
    }


    @Test
    public void fromIncompletedRecipe() {
        Recipe incompletedRecipe = Recipe.builder().completed(false).recipeSteps(stepList).build();
        RecipeResponseDTO detailDTO = RecipeResponseDTO.from(incompletedRecipe, converter);

        assertThat(detailDTO.getRecipeSteps()).containsExactly(
                RecipeStepDTO.from(openedStep, converter), RecipeStepDTO.from(closedStep, converter)
        );
    }
}