package com.woowahan.techcamp.recipehub.recipe.domain;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.common.exception.ForbiddenException;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void isOwner() {
        User user = User.builder().id(1L).build();
        assertTrue(Recipe.builder().owner(user).build().isOwner(user));
        assertFalse(Recipe.builder().owner(user).build().isOwner(User.builder().id(2L).build()));
    }

    @Test
    public void modify() {
        User owner = User.builder().id(1L).build();
        Recipe recipe = Recipe.builder().owner(owner).build();

        RecipeDTO dto = RecipeDTO.builder()
                .name("newName")
                .imgUrl("http://new.com/image.png")
                .build();
        String categoryName = "test";
        Category category = Category.builder().title(categoryName).build();

        recipe.modify(owner, dto, category);

        assertThat(dto).isEqualToIgnoringGivenFields(recipe, "categoryId");
        assertThat(recipe.getCategory().getTitle()).isEqualTo(categoryName);
    }

    @Test
    public void modifyPartialContents() {
        User owner = User.builder().id(1L).build();
        String recipeName = "name";
        Recipe recipe = Recipe.builder()
                .category(Category.builder().build())
                .name(recipeName)
                .owner(owner)
                .build();

        RecipeDTO dto = RecipeDTO.builder()
                .imgUrl("http://new.com/image.png")
                .build();
        String categoryName = "test";

        recipe.modify(owner, dto, null);

        assertThat(recipe.getImgUrl()).isEqualTo(dto.getImgUrl());
        assertThat(recipe.getCategory()).isNotNull();
        assertThat(recipe.getName()).isEqualTo(recipeName);
    }

    @Test(expected = ForbiddenException.class)
    public void modifyByOtherUser() {
        User owner = User.builder().id(1L).build();
        User other = User.builder().id(2L).build();
        Recipe recipe = Recipe.builder().owner(owner).build();

        RecipeDTO dto = RecipeDTO.builder()
                .name("newName")
                .imgUrl("http://new.com/image.png")
                .build();
        String categoryName = "test";
        Category category = Category.builder().title(categoryName).build();

        recipe.modify(other, dto, category);
    }
}
