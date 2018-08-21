package com.woowahan.techcamp.recipehub.recipestep.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeStepCreationDTO {
    private String name;
    private List<String> content;
    private List<Long> ingredients;
    private String imgUrl;
    private Long previousStepId;

    @Builder
    public RecipeStepCreationDTO(String name, List<String> content, List<Long> ingredients, String imgUrl, Long previousStepId) {
        this.name = name;
        this.content = content;
        this.ingredients = ingredients;
        this.imgUrl = imgUrl;
        this.previousStepId = previousStepId;
    }
}
