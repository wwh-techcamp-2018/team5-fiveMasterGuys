package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeDTO;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private RecipeService recipeService;
    private User user;
    private Category category;
    private RecipeDTO dto;

    @Before
    public void setUp() throws Exception {
        // Given
        user = User.builder().name("Ryun").email("ryuneeee@gmail.com").build();
        category = Category.builder().title("양식").build();
        dto = RecipeDTO.builder().name("초코치킨").categoryId(1L).build();
    }

    @Test
    public void create() throws Exception {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        Recipe recipe = dto.toEntity(user, category);
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        // When
        Recipe created = recipeService.create(user, dto);

        // Then
        assertThat(created.getOwner()).isEqualTo(user);
        assertThat(created.getCategory()).isEqualTo(category);
    }

    @Test(expected = BadRequestException.class)
    public void withNullCategory() throws Exception {
        RecipeDTO dtoWithNullCategory = RecipeDTO.builder().name("초코치킨").categoryId(Long.MAX_VALUE).build();
        recipeService.create(user, dtoWithNullCategory);
    }

    @Test(expected = BadRequestException.class)
    public void withNullUser() throws Exception {
        RecipeDTO dtoWithNullCategory = RecipeDTO.builder().name("초코치킨").categoryId(1L).build();
        recipeService.create(null, dtoWithNullCategory);
    }

    @Test
    public void getAllRecipes() {
        recipeService.findAll();
        verify(recipeRepository).findAll();
    }

    @Test
    public void getRecipe() throws Exception {
        long recipeId = 1L;
        Recipe recipe = Recipe.builder().name("초코치킨").build();
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.ofNullable(recipe));
        assertThat(recipeService.findById(recipeId)).isEqualTo(recipe);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNotExistRecipe() {
        recipeService.findById(Long.MAX_VALUE);
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

}