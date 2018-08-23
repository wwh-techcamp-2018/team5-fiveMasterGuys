package com.woowahan.techcamp.recipehub.category.repository;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
