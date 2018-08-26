package com.woowahan.techcamp.recipehub.recipe.dto;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.dto.StepResponseDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeResponseDTOTest {

    private Step openedStep;
    private Step closedStep;

    private List<Step> stepList;

    @Before
    public void setUp() throws Exception {
        openedStep = Step.builder()
                .closed(false)
                .build();
        closedStep = Step.builder()
                .closed(true)
                .build();

        stepList = Arrays.asList(openedStep, closedStep);
    }

    @Test
    public void basic() {
        assertThat(new RecipeResponseDTO()).isInstanceOf(RecipeResponseDTO.class);
    }

    @Test
    public void fromCompletedRecipe() {
        Recipe completedRecipe = Recipe.builder().completed(true).recipeSteps(stepList).build();
        RecipeResponseDTO detailDTO = RecipeResponseDTO.from(completedRecipe);

        assertThat(detailDTO.getRecipeSteps()).containsExactly(StepResponseDTO.from(openedStep));
    }

    @Test
    public void fromIncompletedRecipe() {
        Recipe incompletedRecipe = Recipe.builder().completed(false).recipeSteps(stepList).build();
        RecipeResponseDTO detailDTO = RecipeResponseDTO.from(incompletedRecipe);

        assertThat(detailDTO.getRecipeSteps()).containsExactly(
                StepResponseDTO.from(openedStep)
        );
    }

    @Test
    public void correctField() {
        Recipe incompletedRecipe = Recipe.builder().completed(false)
                .category(new Category())
                .imgUrl("http://asd.com/test.jpg")
                .owner(new User()).recipeSteps(stepList).build();
        RecipeResponseDTO detailDTO = RecipeResponseDTO.from(incompletedRecipe);
        assertThat(detailDTO.getCategory()).isEqualTo(incompletedRecipe.getCategory());
        assertThat(detailDTO.getImgUrl()).isEqualTo(incompletedRecipe.getImgUrl());
        assertThat(detailDTO.getName()).isEqualTo(incompletedRecipe.getName());
        assertThat(detailDTO.getOwner()).isEqualTo(incompletedRecipe.getOwner());
    }
}