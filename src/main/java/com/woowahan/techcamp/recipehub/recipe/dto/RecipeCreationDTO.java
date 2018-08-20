package com.woowahan.techcamp.recipehub.recipe.dto;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCreationDTO {

    @NotNull
    private Long categoryId;

    @NotBlank
    @NotNull
    private String name;

    private String imgUrl;

    @Builder
    public RecipeCreationDTO(@NotNull Long categoryId, @NotBlank @NotNull String name, String imgUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public Recipe toEntity(User owner, Category category) {
        if (owner == null) throw new BadRequestException();

        return Recipe.builder()
                .name(this.name)
                .owner(owner)
                .category(category)
                .imgUrl(this.imgUrl)
                .build();
    }
}
