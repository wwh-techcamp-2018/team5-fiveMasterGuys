package com.woowahan.techcamp.recipehub.recipestep.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RecipeStepServiceProviderTest {
    @Mock
    private UserRecipeStepService userRecipeStepService;

    @Mock
    private OwnerRecipeStepService ownerRecipeStepService;

    @InjectMocks
    private RecipeStepServiceProvider recipeStepServiceProvider;

    @Test
    public void getService() {
        User user = User.builder().id(1L).build();
        User otherUser = User.builder().id(2L).build();
        Recipe recipe = Recipe.builder().owner(user).build();
        assertTrue(recipeStepServiceProvider.getService(recipe, user) instanceof OwnerRecipeStepService);
        assertTrue(recipeStepServiceProvider.getService(recipe, otherUser) instanceof UserRecipeStepService);
    }
}