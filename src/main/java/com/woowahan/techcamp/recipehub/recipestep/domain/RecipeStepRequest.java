package com.woowahan.techcamp.recipehub.recipestep.domain;


import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.dto.RecipeStepCreationDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "RecipeStepRequest")
@DiscriminatorValue("Request")
@Getter
@NoArgsConstructor
public class RecipeStepRequest extends AbstractRecipeStep {

    @ManyToOne
    private RecipeStep target; // nullable (null이면 제일 앞에)

    @Enumerated(value = EnumType.STRING)
    private RequestType requestType;

    @Column
    private boolean rejected;

    @Builder
    public RecipeStepRequest(Long id, Recipe recipe, String name, List<String> content, User writer, String imgUrl,
                             List<Ingredient> ingredients, RecipeStep target, RequestType requestType, boolean rejected) {
        super(id, recipe, name, content, writer, imgUrl, ingredients);
        this.target = target;
        this.requestType = requestType;
        this.rejected = rejected;
    }

    public static RecipeStepRequest from(User user, RecipeStepCreationDTO dto, RecipeStep step, RequestType type) {
        return RecipeStepRequest.builder()
                .writer(user)
                .name(dto.getName())
                .content(dto.getContent())
                .imgUrl(dto.getImgUrl())
                .recipe(step.getRecipe())
                .ingredients(dto.getIngredients())
                .target(step)
                .requestType(type)
                .build();
    }
}
