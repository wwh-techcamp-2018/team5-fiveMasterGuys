package com.woowahan.techcamp.recipehub.recipe.dto;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RecipeDTO {

    @NotNull
    private Category category;

    @NotBlank
    @NotNull
    private String name;

    private String imgUrl;

    @Builder
    public RecipeDTO(@NotNull Category category, @NotEmpty @NotNull String name, String imgUrl) {
        this.category = category;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public Recipe toEntity() {

        return Recipe.builder()
                .name(this.name)
                .category(this.category)
                .imgUrl(this.imgUrl)
                .build();
    }
}
