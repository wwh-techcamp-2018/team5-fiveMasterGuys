package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void getAllRecipes() {
        recipeService.findAll();
        verify(recipeRepository).findAll();
    }

    public void getCompletedRecipe() {
        long recipeId = 1L;
        recipeService.findById(recipeId);
        verify(recipeRepository).findById(recipeId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNotExistRecipe() {
        recipeService.findById(1L);
    }

}