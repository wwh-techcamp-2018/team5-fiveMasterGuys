package com.woowahan.techcamp.recipehub.recipestep.dto;

import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeStepCreationDTO {

    @NotBlank
    private String name;
    private List<String> content;

    @NotNull
    private Long recipeId;
    private List<Ingredient> ingredients;
    private String imgUrl;
    private Long previousStepId;

    @Builder
    public RecipeStepCreationDTO(@NotBlank @NotNull String name, List<String> content, @NotNull Long recipeId, List<Ingredient> ingredients, String imgUrl, Long previousStepId) {
        this.name = name;
        this.content = content;
        this.recipeId = recipeId;
        this.ingredients = ingredients;
        this.imgUrl = imgUrl;
        this.previousStepId = previousStepId;
    }
}
