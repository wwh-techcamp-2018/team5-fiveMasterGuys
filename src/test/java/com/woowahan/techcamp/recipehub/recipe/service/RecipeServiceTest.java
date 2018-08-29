package com.woowahan.techcamp.recipehub.recipe.service;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.category.service.CategoryService;
import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.common.exception.ForbiddenException;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.dto.RecipeDTO;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.step.repository.StepOfferRepository;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private StepOfferRepository stepOfferRepository;

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
        user = User.builder().id(1L).name("Ryun").email("ryuneeee@gmail.com").build();
        category = Category.builder().id(1L).title("양식").build();
        dto = RecipeDTO.builder().categoryId(category.getId()).name("초코치킨").categoryId(1L).build();
    }

    @Test
    public void create() throws Exception {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Recipe recipe = dto.toEntity(user, category);
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        // When
        Recipe created = recipeService.create(user, dto);

        // Then
        assertThat(created.getOwner()).isEqualTo(user);
        assertThat(created.getCategory()).isEqualTo(category);
    }

    @Test(expected = EntityNotFoundException.class)
    public void withNullCategory() throws Exception {
        RecipeDTO dtoWithNullCategory = RecipeDTO.builder().name("초코치킨").categoryId(Long.MAX_VALUE).build();
        recipeService.create(user, dtoWithNullCategory);
    }

    @Test(expected = BadRequestException.class)
    public void withNullUser() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

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

    @Test
    public void completeRecipeOwner() {
        final long recipeId = 1L;
        Recipe recipe = Recipe.builder().name("초코치킨").owner(user).build();
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).then(returnsFirstArg());
        assertThat(recipeService.completeRecipe(user, recipeId).isCompleted()).isTrue();
    }

    @Test(expected = ForbiddenException.class)
    public void completeRecipeNotOwner() {
        final long recipeId = 1L;
        Recipe recipe = Recipe.builder().name("초코치킨").owner(user).build();
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        recipeService.completeRecipe(new User(), recipeId);
    }

    @Test
    public void modify() {
        Recipe recipe = Recipe.builder().name("초코치킨")
                .owner(user)
                .category(Category.builder().title("카테고리").build())
                .build();

        RecipeDTO dto = RecipeDTO.builder()
                .name("newName")
                .categoryId(2L)
                .imgUrl("http://new.com/image.png")
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().title("카테고리2").build()));
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        Recipe modified = recipeService.modify(user, recipe, dto);

        assertThat(dto).isEqualToIgnoringGivenFields(modified, "categoryId");
        assertThat(modified.getCategory().getTitle()).isEqualTo("카테고리2");
    }

    @Test(expected = EntityNotFoundException.class)
    public void searchWithCategory() {
//        when(categoryService.findById(any())).thenReturn();
        recipeService.search(1L, null, null);
    }

    @Test(expected = ForbiddenException.class)
    public void modifyByOtherUser() {
        User otherUser = User.builder().id(user.getId() + 1).build();
        Recipe recipe = Recipe.builder().name("초코치킨")
                .owner(user)
                .category(Category.builder().title("카테고리").build())
                .build();

        RecipeDTO dto = RecipeDTO.builder()
                .name("newName")
                .categoryId(2L)
                .imgUrl("http://new.com/image.png")
                .build();
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().title("카테고리2").build()));


        recipeService.modify(otherUser, recipe, dto);
        verify(recipeRepository, never()).save(any());
    }

    @Test
    public void modifyPartialContents() {
        Category category = Category.builder().title("카테고리").build();
        String recipeName = "초코치킨";
        Recipe recipe = Recipe.builder()
                .name(recipeName)
                .owner(user)
                .category(category)
                .build();

        RecipeDTO dto = RecipeDTO.builder()
                .imgUrl("http://new.com/image.png")
                .build();

        when(recipeRepository.save(recipe)).thenReturn(recipe);

        Recipe result = recipeService.modify(user, recipe, dto);
        assertThat(result.getName()).isEqualTo(recipeName);
        assertThat(result.getImgUrl()).isEqualTo(dto.getImgUrl());
        assertThat(result.getCategory().getTitle()).isEqualTo(category.getTitle());
    }


    @Test(expected = EntityNotFoundException.class)
    public void modifyToNotExistCategory() {
        Category category = Category.builder().title("카테고리").build();
        String recipeName = "초코치킨";
        Recipe recipe = Recipe.builder()
                .name(recipeName)
                .owner(user)
                .category(category)
                .build();

        RecipeDTO dto = RecipeDTO.builder()
                .categoryId(2L)
                .build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        recipeService.modify(user, recipe, dto);
    }
}