package com.woowahan.techcamp.recipehub.step.repository;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StepRepository extends JpaRepository<Step, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Step rs SET rs.sequence = rs.sequence + 1 WHERE rs.recipe = :recipe AND rs.sequence >= :sequence")
    void increaseSequenceGte(@Param("recipe") Recipe recipe, @Param("sequence") Long sequence);
}
