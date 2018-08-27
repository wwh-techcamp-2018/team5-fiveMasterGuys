package com.woowahan.techcamp.recipehub.step.dto;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.user.domain.User;
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
    private Long targetStepId;

    @Builder
    public StepCreationDTO(@NotBlank @NotNull String name, List<String> content, List<Long> ingredients, String imgUrl, Long targetStepId) {
        this.name = name;
        this.content = content;
        this.ingredients = ingredients;
        this.imgUrl = imgUrl;
        this.targetStepId = targetStepId;
    }

    public Step toStep(User user, Recipe recipe, Long sequence) {
        return Step.builder()
                .recipe(recipe)
                .imgUrl(imgUrl)
                .content(content)
                .name(name)
                .ingredients(null)
                .sequence(sequence)
                .closed(false)
                .writer(user)
                .build();
    }
}
