package com.woowahan.techcamp.recipehub.recipe.domain;

public class RecipeStepRequest extends AbstractRecipeStep {
    private RecipeStep target; // nullable (null이면 제일 앞에)
    private RequestType requestType;
    private boolean rejected;
}
