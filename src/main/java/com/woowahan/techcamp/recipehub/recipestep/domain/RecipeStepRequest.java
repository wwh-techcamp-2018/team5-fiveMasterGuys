package com.woowahan.techcamp.recipehub.recipestep.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "RecipeStepRequest")
@DiscriminatorValue("Request")
@Getter
@NoArgsConstructor
public class RecipeStepRequest extends AbstractRecipeStep {
//    private RecipeStep target; // nullable (null이면 제일 앞에)
//    private RequestType requestType;
    private boolean rejected;
}
