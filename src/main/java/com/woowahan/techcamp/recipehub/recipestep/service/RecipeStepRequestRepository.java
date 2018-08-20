package com.woowahan.techcamp.recipehub.recipestep.service;

import com.woowahan.techcamp.recipehub.recipestep.domain.RecipeStepRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRequestRepository extends JpaRepository<RecipeStepRequest, Long> {
}
