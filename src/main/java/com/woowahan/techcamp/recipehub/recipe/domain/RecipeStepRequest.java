package com.woowahan.techcamp.recipehub.recipe.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Entity(name = "RecipeStepRequest")
@DiscriminatorValue("Request")
public class RecipeStepRequest extends AbstractRecipeStep {
//    private RecipeStep target; // nullable (null이면 제일 앞에)
//    private RequestType requestType;
    private boolean rejected;
}
