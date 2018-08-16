package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeDTO;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    public Recipe create(User user, RecipeDTO dto) {
        Recipe recipe = dto.toEntity();
        return recipe;
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }
}
