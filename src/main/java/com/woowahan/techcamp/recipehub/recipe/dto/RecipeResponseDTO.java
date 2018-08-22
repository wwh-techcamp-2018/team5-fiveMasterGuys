package com.woowahan.techcamp.recipehub.recipe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.dto.StepResponseDTO;
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

    private List<StepResponseDTO> recipeSteps;

    private User owner;


    @Builder
    public RecipeResponseDTO(Category category, String name, String imgUrl, List<StepResponseDTO> recipeSteps, User owner) {
        this.category = category;
        this.name = name;
        this.imgUrl = imgUrl;
        this.recipeSteps = recipeSteps;
        this.owner = owner;
    }


    public static RecipeResponseDTO from(Recipe recipe) {
        Stream<Step> stepStream = recipe.getRecipeSteps().stream();

        if (recipe.isCompleted()) {
            stepStream = stepStream.filter(step -> !step.isClosed());
        }

        return RecipeResponseDTO.builder()
                .name(recipe.getName())
                .category(recipe.getCategory())
                .imgUrl(recipe.getImgUrl())
                .recipeSteps(
                        stepStream.map(StepResponseDTO::from).collect(Collectors.toList())
                )
                .build();
    }
}
