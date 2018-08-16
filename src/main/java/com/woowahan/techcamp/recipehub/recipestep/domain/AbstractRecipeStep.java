package com.woowahan.techcamp.recipehub.recipestep.domain;

import com.woowahan.techcamp.recipehub.ingredient.domain.Ingredient;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "RecipeStep")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
@Getter
public abstract class AbstractRecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recipe recipe;
    private String name;
    private String content;

    @ManyToOne
    private User writer;
    private String imgUrl;

    @ManyToMany
    private List<Ingredient> ingredients;
}
