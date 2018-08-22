package com.woowahan.techcamp.recipehub.step.dto;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StepResponseDTOTest {

    private Step.StepBuilder recipeStepBuilder;

    @Before
    public void setUp() throws Exception {
        List<String> expectedStepContents = Arrays.asList("식빵을 토스터기에 굽는다", "잼을 바른다");
        recipeStepBuilder = Step.builder()
                .id(1L)
                .name("잼을 바른 토스트")
                .closed(false)
                .content(expectedStepContents)
                .imgUrl("http://imgurl.com/static/img/image.jpg")
                .ingredients(new ArrayList<>())
                .sequence(1L)
                .writer(User.builder().id(1L).build())
                .recipe(Recipe.builder().build());
    }

    @Test
    public void from() {
        Step recipeStep = recipeStepBuilder.build();
        StepResponseDTO stepResponseDTO = StepResponseDTO.from(recipeStep);

        assertRecipeStepDtoEqualToRecipe(stepResponseDTO, recipeStep);
    }

    @Test
    public void fromEmptyContents() {
        Step recipeStep = recipeStepBuilder.content(new ArrayList<>()).build();
        StepResponseDTO stepResponseDTO = StepResponseDTO.from(recipeStep);

        assertRecipeStepDtoEqualToRecipe(stepResponseDTO, recipeStep);
    }

    private void assertRecipeStepDtoEqualToRecipe(StepResponseDTO stepResponseDTO, Step recipeStep) {
        assertThat(stepResponseDTO.getContent()).isEqualTo(recipeStep.getContent());
        assertThat(stepResponseDTO.getId()).isEqualTo(recipeStep.getId());
        assertThat(stepResponseDTO.getName()).isEqualTo(recipeStep.getName());
        assertThat(stepResponseDTO.isClosed()).isEqualTo(recipeStep.isClosed());
        assertThat(stepResponseDTO.getImgUrl()).isEqualTo(recipeStep.getImgUrl());
        assertThat(stepResponseDTO.getIngredients()).isEqualTo(recipeStep.getIngredients());
        assertThat(stepResponseDTO.getSequence()).isEqualTo(recipeStep.getSequence());
        assertThat(stepResponseDTO.getWriter()).isEqualTo(recipeStep.getWriter());
        assertThat(stepResponseDTO.getRecipe()).isEqualTo(recipeStep.getRecipe());
    }
}
