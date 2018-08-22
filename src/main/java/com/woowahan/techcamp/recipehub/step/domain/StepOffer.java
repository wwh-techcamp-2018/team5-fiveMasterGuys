package com.woowahan.techcamp.recipehub.step.domain;


import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "StepOffer")
@DiscriminatorValue("Request")
@Getter
@NoArgsConstructor
public class StepOffer extends AbstractStep {

    @ManyToOne
    private Step target;

    @Enumerated(value = EnumType.STRING)
    private OfferType offerType;

    @Column
    private boolean rejected;

    @Builder
    public StepOffer(Long id, Recipe recipe, String name, List<String> content, User writer, String imgUrl,
                     List<Ingredient> ingredients, Step target, OfferType offerType, boolean rejected) {
        super(id, recipe, name, content, writer, imgUrl, ingredients);
        this.target = target;
        this.offerType = offerType;
        this.rejected = rejected;
    }

    public static StepOffer from(User user, StepCreationDTO dto, Recipe recipe, Step target, OfferType type) {
        return StepOffer.builder()
                .writer(user)
                .name(dto.getName())
                .content(dto.getContent())
                .imgUrl(dto.getImgUrl())
                .recipe(recipe)
                .target(target)
                .ingredients(null)
                .offerType(type)
                .build();
    }
}
