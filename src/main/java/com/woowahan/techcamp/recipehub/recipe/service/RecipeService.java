package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeDTO;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.step.domain.StepOffer;
import com.woowahan.techcamp.recipehub.step.repository.StepOfferRepository;
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

    @Autowired
    private StepOfferRepository stepOfferRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Recipe create(User owner, RecipeDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(BadRequestException::new);
        return recipeRepository.save(dto.toEntity(owner, category));
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Page<Recipe> findAllByPageable(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public List<StepOffer> findNullTargetStepOffersByRecipe(Recipe recipe) {
        return stepOfferRepository.findAllByRecipeAndTargetIsNull(recipe);
    }
}
