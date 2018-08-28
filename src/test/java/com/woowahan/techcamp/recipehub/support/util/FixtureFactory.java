package com.woowahan.techcamp.recipehub.support.util;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;

import java.util.ArrayList;
import java.util.List;

public class FixtureFactory {
    public static final String RECIPE_NAME_PREFIX = "recipe";

    public static List<Recipe> generateRecipeList(Category category, int count) {
        List<Recipe> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(Recipe.builder()
                    .name(String.format("%s %d", RECIPE_NAME_PREFIX, i))
                    .category(category)
                    .completed(i % 2 == 0)
                    .imgUrl(String.format("img %d", i))
                    .build());
        }

        return result;
    }
}
