package com.woowahan.techcamp.recipehub.recipe.service;


import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.woowahan.techcamp.recipehub.support.util.FixtureFactory.generateRecipeList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeListServiceTest {

    private static final int PAGE_NUMBER = 3;
    private static final int PAGE_SIZE = 9;

    private static final String SORT_PROPERTY = "id";

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Sort sort;
    private List<Recipe> lastRecipeList;
    private Page<Recipe> resultRecipePage;

    @Before
    public void createPageInfo() throws Exception {
        lastRecipeList = generateRecipeList(5);

        sort = new Sort(Sort.Direction.ASC, SORT_PROPERTY);
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, sort);

        when(recipeRepository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(lastRecipeList, pageRequest, PAGE_NUMBER * PAGE_SIZE + lastRecipeList.size()));
        resultRecipePage = recipeService.findAllByPagable(pageRequest);
    }

    @Test
    public void getCorrectPageNumber() {
        assertThat(resultRecipePage.getNumber()).isEqualTo(PAGE_NUMBER);
    }

    @Test
    public void getCorrectPageSize() {
        assertThat(resultRecipePage.getSize()).isEqualTo(PAGE_SIZE);
    }

    @Test
    public void getCorrectPageContent() {
        assertThat(resultRecipePage.getContent()).isEqualTo(lastRecipeList);
    }

    @Test
    public void getCorrectPageThatSortedAscendingOrderByNameProperty() {
        assertThat(resultRecipePage.getSort().getOrderFor(SORT_PROPERTY).getDirection())
                .isEqualTo(Sort.Direction.ASC);
    }
}
