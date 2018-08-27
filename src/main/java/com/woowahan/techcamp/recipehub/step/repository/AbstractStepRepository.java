package com.woowahan.techcamp.recipehub.step.repository;

import com.woowahan.techcamp.recipehub.step.domain.AbstractStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractStepRepository extends JpaRepository<AbstractStep, Long> {
}
