package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.service.CategoryService;
import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.common.exception.ForbiddenException;
import com.woowahan.techcamp.recipehub.common.exception.NotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private StepOfferRepository stepOfferRepository;

    @Autowired
    private CategoryService categoryService;

    public Recipe create(User owner, RecipeDTO dto) {
        Category category = categoryService.findById(dto.getCategoryId()).orElseThrow(BadRequestException::new);
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

    @Transactional
    public Recipe completeRecipe(User user, long recipeId) {
        Recipe recipe = findById(recipeId);
        if (!recipe.isOwner(user)) throw new ForbiddenException();

        stepOfferRepository.rejectAllOffersByRecipe(recipe);
        recipe.complete();
        return recipeRepository.save(recipe);
    }

    public Recipe modify(User user, Recipe recipe, RecipeDTO dto) {
        Category category = getCategoryIfIdExist(dto.getCategoryId());
        recipe.modify(user, dto, category);
        return recipeRepository.save(recipe);
    }

    public Page<Recipe> search(Long categoryId, String keyword, Pageable pageable) throws NotFoundException {
        return recipeRepository.findByCategoryAndNameContaining(
                categoryService.findById(categoryId).orElseThrow(NotFoundException::new),
                keyword,
                pageable);
    }

    public Page<Recipe> search(String keyword, Pageable pageable) {
        return recipeRepository.findByNameContaining(
                keyword,
                pageable);
    }
  
    private Category getCategoryIfIdExist(Long categoryId) {
        if (categoryId != null) {
            return categoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        }
        return null;
    }

}
