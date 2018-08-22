package com.woowahan.techcamp.recipehub.step.service;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class StepServiceProviderTest {
    @Mock
    private StepOfferService userRecipeStepService;

    @Mock
    private StepOwnerService stepOwnerService;

    @InjectMocks
    private StepServiceProvider stepServiceProvider;

    @Test
    public void getService() {
        User user = User.builder().id(1L).build();
        User otherUser = User.builder().id(2L).build();
        Recipe recipe = Recipe.builder().owner(user).build();
        assertTrue(stepServiceProvider.getService(recipe, user) instanceof StepOwnerService);
        assertTrue(stepServiceProvider.getService(recipe, otherUser) instanceof StepOfferService);
    }
}