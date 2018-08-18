package com.woowahan.techcamp.recipehub.recipestep.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeStepDTOTest {

    private RecipeStep.RecipeStepBuilder recipeStepBuilder;
    private List<String> expectedStepContents;

    @Before
    public void setUp() throws Exception {
        expectedStepContents = Arrays.asList("식빵을 토스터기에 굽는다", "잼을 바른다");
        recipeStepBuilder = RecipeStep.builder()
                .id(1L)
                .name("잼을 바른 토스트")
                .closed(false)
                .content(toJsonArrayString(expectedStepContents))
                .imgUrl("http://imgurl.com/static/img/image.jpg")
                .ingredients(new ArrayList<>())
                .sequence(1L)
                .writer(User.builder().id(1L).build())
                .recipe(Recipe.builder().build());
    }

    @Test
    public void from() {
        RecipeStep recipeStep = recipeStepBuilder.build();
        RecipeStepDTO recipeStepDTO = RecipeStepDTO.from(recipeStep);

        assertRecipeStepDtoEqualToRecipe(recipeStepDTO, recipeStep);
    }

    @Test
    public void fromEmptyContents() {
        expectedStepContents = new ArrayList<>();
        RecipeStep recipeStep = recipeStepBuilder.content(toJsonArrayString(expectedStepContents)).build();
        RecipeStepDTO recipeStepDTO = RecipeStepDTO.from(recipeStep);

        assertRecipeStepDtoEqualToRecipe(recipeStepDTO, recipeStep);
    }

    @Test
    public void toJsonArrayStringTest() {
        assertThat(toJsonArrayString(expectedStepContents)).isEqualTo("[\"식빵을 토스터기에 굽는다\",\"잼을 바른다\"]");
    }

    @Test
    public void readValueTest() throws IOException {
        assertThat(new ObjectMapper().readValue("[\"식빵을 토스터기에 굽는다\",\"잼을 바른다\"]", List.class))
                .isEqualTo(expectedStepContents);
    }

    private void assertRecipeStepDtoEqualToRecipe(RecipeStepDTO recipeStepDTO, RecipeStep recipeStep) {
        assertThat(recipeStepDTO.getContent()).isEqualTo(expectedStepContents);
        assertThat(recipeStepDTO.getId()).isEqualTo(recipeStep.getId());
        assertThat(recipeStepDTO.getName()).isEqualTo(recipeStep.getName());
        assertThat(recipeStepDTO.isClosed()).isEqualTo(recipeStep.isClosed());
        assertThat(recipeStepDTO.getImgUrl()).isEqualTo(recipeStep.getImgUrl());
        assertThat(recipeStepDTO.getIngredients()).isEqualTo(recipeStep.getIngredients());
        assertThat(recipeStepDTO.getSequence()).isEqualTo(recipeStep.getSequence());
        assertThat(recipeStepDTO.getWriter()).isEqualTo(recipeStep.getWriter());
        assertThat(recipeStepDTO.getRecipe()).isEqualTo(recipeStep.getRecipe());
    }

    private String toJsonArrayString(List<String> stringList) {
        try {
            return new ObjectMapper().writeValueAsString(stringList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json parse error");
        }
    }
}
