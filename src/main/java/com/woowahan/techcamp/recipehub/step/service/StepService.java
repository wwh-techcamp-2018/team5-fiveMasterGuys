package com.woowahan.techcamp.recipehub.step.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.AbstractStep;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.step.dto.StepCreationDTO;
import com.woowahan.techcamp.recipehub.user.domain.User;

public interface StepService {
    AbstractStep create(User user, StepCreationDTO dto, Recipe recipe);

    AbstractStep modify(User user, long targetId, StepCreationDTO dto, Recipe recipe);

    Step approve(Recipe recipe, Long offerId, User user);
}
