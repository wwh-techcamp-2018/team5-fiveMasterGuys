package com.woowahan.techcamp.recipehub.step.domain;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
import com.woowahan.techcamp.recipehub.step.repository.StepRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StepRepositoryTest {
    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    public void increaseSequences() {
        Recipe recipe = recipeRepository.save(
                Recipe.builder()
                        .name("a")
                        .category(categoryRepository.save(new Category("category")))
                        .completed(false)
                        .build()
        );

        List<Step> recipeSteps = Arrays.asList(
                Step.builder().recipe(recipe).sequence(1L).build(),
                Step.builder().recipe(recipe).sequence(2L).build(),
                Step.builder().recipe(recipe).sequence(3L).build(),
                Step.builder().recipe(recipe).sequence(4L).build()
        );
        stepRepository.saveAll(recipeSteps);
        stepRepository.increaseSequenceGte(recipe, 2L);
        List<Step> updated = stepRepository.findAll();
        assertThat(updated.stream().mapToLong(Step::getSequence)).containsExactly(1L, 3L, 4L, 5L);

    }

    @After
    public void tearDown() throws Exception {
        stepRepository.deleteAll();
        recipeRepository.deleteAll();
    }
}
