package com.woowahan.techcamp.recipehub.recipe.repository;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findByCategoryAndNameContaining(Category category, String name, Pageable pageable);

    Page<Recipe> findByNameContaining(String name, Pageable pageable);
}
