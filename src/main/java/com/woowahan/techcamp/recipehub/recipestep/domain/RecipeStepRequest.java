package com.woowahan.techcamp.recipehub.recipestep.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "RecipeStepRequest")
@DiscriminatorValue("Request")
public class RecipeStepRequest extends AbstractRecipeStep {
    //    private RecipeStep target; // nullable (null이면 제일 앞에)
//    private RequestType requestType;
    private boolean rejected;
}
