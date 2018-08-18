package com.woowahan.techcamp.recipehub.recipestep.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

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

    public static RecipeStepDTO from(RecipeStep recipeStep) {
        return RecipeStepDTO.builder()
                .id(recipeStep.getId())
                .name(recipeStep.getName())
                .closed(recipeStep.isClosed())
                .content(convertStringToJsonArray(recipeStep.getContent()))
                .imgUrl(recipeStep.getImgUrl())
                .ingredients(recipeStep.getIngredients())
                .sequence(recipeStep.getSequence())
                .writer(recipeStep.getWriter())
                .recipe(recipeStep.getRecipe())
                .build();
    }

    private static List<String> convertStringToJsonArray(String content) {
        try {
            return new ObjectMapper().readValue(content, List.class);
        } catch (IOException e) {
            throw new RuntimeException("JSON parse error");
        }
    }
}
