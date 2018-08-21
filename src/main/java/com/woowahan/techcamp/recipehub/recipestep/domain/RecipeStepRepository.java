package com.woowahan.techcamp.recipehub.recipestep.domain;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE RecipeStep rs SET rs.sequence = rs.sequence + 1 WHERE rs.recipe = :recipe AND rs.sequence >= :sequence")
    void increaseSequenceGte(@Param("recipe") Recipe recipe, @Param("sequence") Long sequence);
}
