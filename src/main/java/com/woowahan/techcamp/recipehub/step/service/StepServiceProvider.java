package com.woowahan.techcamp.recipehub.step.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StepServiceProvider {

    @Autowired
    private StepOwnerService stepOwnerService;

    @Autowired
    private StepOfferService stepOfferService;

    public StepService getService(Recipe recipe, User user) {
        if (recipe.isOwner(user)) {
            return stepOwnerService;
        }
        return stepOfferService;
    }
}
