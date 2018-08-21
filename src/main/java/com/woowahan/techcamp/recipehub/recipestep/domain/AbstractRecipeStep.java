package com.woowahan.techcamp.recipehub.recipestep.domain;

import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipestep.converter.RecipeStepContentConverter;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "RecipeStep")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@NoArgsConstructor
public abstract class AbstractRecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recipe recipe;
    private String name;

    @Convert(converter = RecipeStepContentConverter.class)
    private List<String> content;

    @ManyToOne
    private User writer;
    private String imgUrl;

    @ManyToMany
    private List<Ingredient> ingredients;

    public AbstractRecipeStep(Long id, Recipe recipe, String name, List<String> content, User writer, String imgUrl, List<Ingredient> ingredients) {
        this.id = id;
        this.recipe = recipe;
        this.name = name;
        this.content = content;
        this.writer = writer;
        this.imgUrl = imgUrl;
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractRecipeStep)) return false;
        AbstractRecipeStep that = (AbstractRecipeStep) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
