package com.woowahan.techcamp.recipehub.category.service;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.config.CachingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Cacheable(value = CachingConfig.CATEGORIES)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
    }
}
