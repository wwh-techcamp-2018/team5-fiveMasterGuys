package com.woowahan.techcamp.recipehub.recipe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeResponseDTO {

    private Category category;

    private String name;

    private String imgUrl;

    private List<RecipeStep> recipeSteps;

    private User owner;


    @Builder
    public RecipeResponseDTO(Category category, String name, String imgUrl, List<RecipeStep> recipeSteps, User owner) {
        this.category = category;
        this.name = name;
        this.imgUrl = imgUrl;
        this.recipeSteps = recipeSteps;
        this.owner = owner;
    }


    public static RecipeResponseDTO from(Recipe recipe) {
        RecipeResponseDTOBuilder builder = RecipeResponseDTO.builder()
                .name(recipe.getName())
                .category(recipe.getCategory())
                .imgUrl(recipe.getImgUrl())
                .recipeSteps(recipe.getRecipeSteps());

        if (recipe.isCompleted()) {
            builder.recipeSteps(recipe.getRecipeSteps().stream()
                    .filter(step -> !step.isClosed())
                    .collect(Collectors.toList()));
        }

        return builder.build();
    }
}
