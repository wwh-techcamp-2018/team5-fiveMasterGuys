package com.woowahan.techcamp.recipehub.recipestep.dto;

import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.util.RecipeStepContentConverter;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class RecipeStepDTO {

    private Long id;

    private Recipe recipe;
    private String name;
    private List<String> content;

    private User writer;
    private String imgUrl;

    private List<Ingredient> ingredients;

    private Long sequence;
    private boolean closed;

    @Builder
    public RecipeStepDTO(Long id, Recipe recipe, String name, List<String> content, User writer, String imgUrl, List<Ingredient> ingredients, Long sequence, boolean closed) {
        this.id = id;
        this.recipe = recipe;
        this.name = name;
        this.content = content;
        this.writer = writer;
        this.imgUrl = imgUrl;
        this.ingredients = ingredients;
        this.sequence = sequence;
        this.closed = closed;
    }

    public static RecipeStepDTO from(RecipeStep recipeStep, RecipeStepContentConverter converter) {
        return RecipeStepDTO.builder()
                .id(recipeStep.getId())
                .name(recipeStep.getName())
                .closed(recipeStep.isClosed())
                .content(converter.toList(recipeStep.getContent()))
                .imgUrl(recipeStep.getImgUrl())
                .ingredients(recipeStep.getIngredients())
                .sequence(recipeStep.getSequence())
                .writer(recipeStep.getWriter())
                .recipe(recipeStep.getRecipe())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStepDTO that = (RecipeStepDTO) o;
        return closed == that.closed &&
                Objects.equals(id, that.id) &&
                Objects.equals(recipe, that.recipe) &&
                Objects.equals(name, that.name) &&
                Objects.equals(content, that.content) &&
                Objects.equals(writer, that.writer) &&
                Objects.equals(imgUrl, that.imgUrl) &&
                Objects.equals(ingredients, that.ingredients) &&
                Objects.equals(sequence, that.sequence);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, recipe, name, content, writer, imgUrl, ingredients, sequence, closed);
    }
}
