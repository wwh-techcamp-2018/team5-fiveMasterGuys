package com.woowahan.techcamp.recipehub.recipe.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeTest {

    @Test
    public void createRecipe() throws Exception {
        Recipe recipe = Recipe.builder().build();
        assertThat(recipe).isEqualTo(recipe);
    }
}
