package com.woowahan.techcamp.recipehub.recipe.domain;

import javax.persistence.*;

@Entity
public class RecipeStep extends AbstractRecipeStep {
    private Long order;
    private boolean closed;
}
