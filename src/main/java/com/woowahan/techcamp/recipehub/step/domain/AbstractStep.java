package com.woowahan.techcamp.recipehub.step.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.converter.StepContentConverter;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Step")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@NoArgsConstructor
public abstract class AbstractStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Recipe recipe;

    @JsonGetter("recipeId")
    public Long getRecipeId() {
        return recipe.getId();
    }

    private String name;

    @Convert(converter = StepContentConverter.class)
    private List<String> content;

    @ManyToOne
    private User writer;
    private String imgUrl;

    @ManyToMany
    private List<Ingredient> ingredients;

    public AbstractStep(Long id, Recipe recipe, String name, List<String> content, User writer, String imgUrl, List<Ingredient> ingredients) {
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
        if (!(o instanceof AbstractStep)) return false;
        AbstractStep that = (AbstractStep) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
