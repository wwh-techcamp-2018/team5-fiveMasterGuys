package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    private static List<Recipe> generateRecipeList(int count) {
        List<Recipe> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(Recipe.builder()
                    .name(String.format("recipe %d", i))
                    .build());
        }

        return result;
    }

    public void getCompletedRecipe() {
        long recipeId = 1L;
        Recipe recipe = spy(Recipe.builder().recipeSteps(new ArrayList<>()).completed(true).build());
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        Recipe result = recipeService.findById(recipeId);

        assertThat(result).isEqualTo(recipe);
        verify(recipe).removeClosedSteps();
    }


    @Test
    public void getIncompletedRecipe() {
        long recipeId = 1L;
        Recipe recipe = spy(Recipe.builder().recipeSteps(new ArrayList<>()).completed(false).build());
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        Recipe result = recipeService.findById(recipeId);

        assertThat(result).isEqualTo(recipe);
        verify(recipe, never()).removeClosedSteps();
    }


    @Test(expected = EntityNotFoundException.class)
    public void getNotExistRecipe() {
        recipeService.findById(1L);
    }
}