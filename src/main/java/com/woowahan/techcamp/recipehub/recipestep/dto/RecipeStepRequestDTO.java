package com.woowahan.techcamp.recipehub.recipestep.dto;

import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStepRequest;
import com.woowahan.techcamp.recipehub.recipestep.domain.RequestType;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class RecipeStepRequestDTO {

    private Long id;

    private Recipe recipe;
    private String name;
    private List<String> content;

    private User writer;
    private String imgUrl;

    private List<Ingredient> ingredients;

    private RecipeStep target;
    private RequestType requestType;
    private boolean rejected;

    @Builder
    public RecipeStepRequestDTO(Long id, Recipe recipe, String name, List<String> content, User writer, String imgUrl, List<Ingredient> ingredients, RecipeStep target, RequestType requestType, boolean rejected) {
        this.id = id;
        this.recipe = recipe;
        this.name = name;
        this.content = content;
        this.writer = writer;
        this.imgUrl = imgUrl;
        this.ingredients = ingredients;
        this.target = target;
        this.requestType = requestType;
        this.rejected = rejected;
    }

    public static RecipeStepRequestDTO from(RecipeStepRequest recipeStepRequest) {
        return RecipeStepRequestDTO.builder()
                .id(recipeStepRequest.getId())
                .name(recipeStepRequest.getName())
                .content(recipeStepRequest.getContent())
                .imgUrl(recipeStepRequest.getImgUrl())
                .ingredients(recipeStepRequest.getIngredients())
                .writer(recipeStepRequest.getWriter())
                .recipe(recipeStepRequest.getRecipe())
                .target(recipeStepRequest.getTarget())
                .requestType(recipeStepRequest.getRequestType())
                .rejected(recipeStepRequest.isRejected())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStepRequestDTO that = (RecipeStepRequestDTO) o;
        return rejected == that.rejected &&
                Objects.equals(id, that.id) &&
                Objects.equals(recipe, that.recipe) &&
                Objects.equals(name, that.name) &&
                Objects.equals(content, that.content) &&
                Objects.equals(writer, that.writer) &&
                Objects.equals(imgUrl, that.imgUrl) &&
                Objects.equals(ingredients, that.ingredients) &&
                Objects.equals(target, that.target) &&
                requestType == that.requestType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, recipe, name, content, writer, imgUrl, ingredients, target, requestType, rejected);
    }
}
