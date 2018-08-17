package com.woowahan.techcamp.recipehub.recipe.domain;

import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeTest {

    @Test
    public void createRecipe() throws Exception {
        Recipe recipe = Recipe.builder().build();
        assertThat(recipe).isEqualTo(recipe);
    }

    @Test
    public void createWithUser() {
        User owner = User.builder().id(1L).build();
        User other = User.builder().id(2L).build();
        Recipe recipe = Recipe.builder().owner(owner).build();
        assertThat(recipe.getOwner()).isEqualTo(owner);
        assertThat(recipe.getOwner()).isNotEqualTo(other);
    }

}
