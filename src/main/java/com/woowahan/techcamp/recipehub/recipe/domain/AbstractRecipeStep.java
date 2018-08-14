package com.woowahan.techcamp.recipehub.recipe.domain;

import com.woowahan.techcamp.recipehub.user.domain.User;

import java.util.List;

public abstract class AbstractRecipeStep {
    private Long id;
    private Recipe recipe;
    private String name;
    private String content;
    private User writer;
    private String imgUrl;
    private List<Ingredient> ingredients;
}
