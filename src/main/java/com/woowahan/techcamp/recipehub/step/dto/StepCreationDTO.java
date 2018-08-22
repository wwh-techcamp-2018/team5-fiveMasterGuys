package com.woowahan.techcamp.recipehub.step.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class StepCreationDTO {

    @NotBlank
    private String name;
    private List<String> content;
    private List<Long> ingredients;
    private String imgUrl;
    private Long previousStepId;

    @Builder
    public StepCreationDTO(@NotBlank @NotNull String name, List<String> content, List<Long> ingredients, String imgUrl, Long previousStepId) {
        this.name = name;
        this.content = content;
        this.ingredients = ingredients;
        this.imgUrl = imgUrl;
        this.previousStepId = previousStepId;
    }
}
