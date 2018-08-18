package com.woowahan.techcamp.recipehub.recipe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStep;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepDTO;
import com.woowahan.techcamp.recipehub.recipestep.util.RecipeStepContentConverter;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeResponseDTO {

    private Category category;

    private String name;

    private String imgUrl;

    private List<RecipeStepDTO> recipeSteps;

    private User owner;


    @Builder
    public RecipeResponseDTO(Category category, String name, String imgUrl, List<RecipeStepDTO> recipeSteps, User owner) {
        this.category = category;
        this.name = name;
        this.imgUrl = imgUrl;
        this.recipeSteps = recipeSteps;
        this.owner = owner;
    }


    public static RecipeResponseDTO from(Recipe recipe, RecipeStepContentConverter converter) {
        Stream<RecipeStep> stepStream = recipe.getRecipeSteps().stream();

        if (recipe.isCompleted()) {
            stepStream = stepStream.filter(step -> !step.isClosed());
        }

        return RecipeResponseDTO.builder()
                .name(recipe.getName())
                .category(recipe.getCategory())
                .imgUrl(recipe.getImgUrl())
                .recipeSteps(
                        stepStream.map(step -> RecipeStepDTO.from(step, converter)).collect(Collectors.toList())
                )
                .build();
    }
}
