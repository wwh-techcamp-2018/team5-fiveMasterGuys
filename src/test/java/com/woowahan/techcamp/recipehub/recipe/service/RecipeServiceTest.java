package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {
    
    @Mock
    private RecipeRepository recipeRepository;
    
    @InjectMocks
    private RecipeService recipeService;

    @Test
    public void getAllRecipes() {
        List<Recipe> recipes = generateRecipeList(5);
        when(recipeRepository.findAll()).thenReturn(recipes);

        assertThat(recipeService.findAll()).hasSameElementsAs(recipes);
    }

    private static List<Recipe> generateRecipeList(int count) {
        List<Recipe> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(Recipe.builder()
                    .name("recipe " + i)
                    .build());
        }

        return result;
    }
}
