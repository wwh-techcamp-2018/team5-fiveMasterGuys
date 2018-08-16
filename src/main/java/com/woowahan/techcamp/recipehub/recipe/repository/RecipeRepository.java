package com.woowahan.techcamp.recipehub.recipe.repository;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
