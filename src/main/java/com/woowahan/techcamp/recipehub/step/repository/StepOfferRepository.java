package com.woowahan.techcamp.recipehub.step.repository;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.step.domain.AbstractStep;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StepOfferRepository extends JpaRepository<StepOffer, Long> {

    List<StepOffer> findAllByRecipeAndTargetIsNull(Recipe recipe);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE StepOffer so SET so.target = :target " +
            "WHERE so.target = :prevStep AND so.offerType = 'APPEND'")
    void changeAppendOffersTarget(@Param("prevStep") AbstractStep prevStep, @Param("target") AbstractStep target);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE StepOffer so SET so.rejected = true " +
            "WHERE so.target = :target AND so.offerType = 'MODIFY'")
    void rejectModifyingOfferByTarget(@Param("target") AbstractStep target);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE StepOffer so SET so.rejected = true WHERE so.recipe = :recipe")
    void rejectAllOffersByRecipe(@Param("recipe") Recipe recipe);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE StepOffer so SET so.rejected = true " +
            "WHERE so.target IS NOT NULL AND so.target = :target AND so.offerType = 'APPEND'")
    void rejectAppendingOffersByTarget(@Param("target") AbstractStep target);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE StepOffer so SET so.rejected = true " +
            "WHERE so.target IS NULL AND so.recipe = :recipe")
    void rejectAppendingOffersByNullTarget(@Param("recipe") Recipe recipe);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE step s " +
            "SET s.TYPE = 'Step', s.CLOSED = false, s.SEQUENCE = :seq, s.TARGET_ID = NULL, s.OFFER_TYPE = NULL, s.REJECTED = NULL " +
            "WHERE s.ID = :id",
            nativeQuery = true)
    void approveStepOffer(@Param("id") Long offerId, @Param("seq") Long sequence);
}
