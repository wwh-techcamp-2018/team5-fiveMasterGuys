package com.woowahan.techcamp.recipehub.step.domain;

import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "Step")
@DiscriminatorValue("Step")
@Getter
@NoArgsConstructor
public class Step extends AbstractStep {
    private Long sequence;
    private boolean closed;

    @Builder
    public Step(Long id, Recipe recipe, String name, List<String> content, User writer, String imgUrl, List<Ingredient> ingredients, Long sequence, boolean closed) {
        super(id, recipe, name, content, writer, imgUrl, ingredients);
        this.sequence = sequence;
        this.closed = closed;
    }
}
