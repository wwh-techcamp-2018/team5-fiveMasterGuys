package com.woowahan.techcamp.recipehub.recipestep.domain;


import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "RecipeStepRequest")
@DiscriminatorValue("Request")
@Getter
public class RecipeStepRequest extends AbstractRecipeStep {
//    private RecipeStep target; // nullable (null이면 제일 앞에)
//    private RequestType requestType;
    private boolean rejected;
}
