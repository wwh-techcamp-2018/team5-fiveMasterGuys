package com.woowahan.techcamp.recipehub.step.dto;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
public class StepCreationDTO {
    private static final int MAX_CONTENT_LENGTH = 45;

    @Size(min = 1, max = 30)
    @NotBlank
    @NotNull
    private String name;

    @Size(max = 5)
    private List<String> content;

    private List<Long> ingredients;

    private String imgUrl;
    private Long targetStepId;

    @AssertTrue
    private boolean isContentLengthExceeded() {
        for (String item : content) {
            if (item.length() > MAX_CONTENT_LENGTH) {
                return false;
            }
        }
        return true;
    }

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
