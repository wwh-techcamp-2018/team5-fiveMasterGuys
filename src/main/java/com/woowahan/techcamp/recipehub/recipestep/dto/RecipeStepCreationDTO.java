package com.woowahan.techcamp.recipehub.recipestep.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeStepCreationDTO {
    private String name;
    private List<String> contents;
    private Long recipeId;
    private List<Long> ingredients;
    private String imgUrl;
}
