package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeCreationDTO;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe create(User user, RecipeCreationDTO dto) {
        Recipe recipe = dto.toEntity();
        return recipe;
    }

    public Recipe findById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Page<Recipe> findAllByPagable(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }
}
