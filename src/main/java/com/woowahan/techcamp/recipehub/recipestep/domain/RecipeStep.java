package com.woowahan.techcamp.recipehub.recipestep.domain;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "RecipeStep")
@DiscriminatorValue("Step")
@Getter
public class RecipeStep extends AbstractRecipeStep {
    private Long sequence;
    private boolean closed;
}
