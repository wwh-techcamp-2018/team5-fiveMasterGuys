package com.woowahan.techcamp.recipehub.support.util;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FixtureFactory {

    public static List<Recipe> generateRecipeList(int count) {
        List<Recipe> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(Recipe.builder()
                    .name(String.format("recipe %d", i))
                    .completed(i % 2 == 0)
                    .imgUrl(String.format("img %d", i))
                    .build());
        }

        return result;
    }
}
