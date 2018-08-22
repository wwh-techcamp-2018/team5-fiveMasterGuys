package com.woowahan.techcamp.recipehub.step.repository;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepOfferRepository extends JpaRepository<StepOffer, Long> {

    List<StepOffer> findAllByRecipeAndTargetIsNull(Recipe recipe);
}
