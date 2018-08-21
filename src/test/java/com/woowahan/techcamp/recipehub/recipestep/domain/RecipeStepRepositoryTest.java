package com.woowahan.techcamp.recipehub.recipestep.domain;

import com.woowahan.techcamp.recipehub.recipe.domain.Recipe;
import com.woowahan.techcamp.recipehub.recipe.repository.RecipeRepository;
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
public class RecipeStepRepositoryTest {
    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @Transactional
    public void increaseSequences() {
        Recipe recipe = recipeRepository.save(
                Recipe.builder().name("a").completed(false).build()
        );

        List<RecipeStep> recipeSteps = Arrays.asList(
                RecipeStep.builder().recipe(recipe).sequence(1L).build(),
                RecipeStep.builder().recipe(recipe).sequence(2L).build(),
                RecipeStep.builder().recipe(recipe).sequence(3L).build(),
                RecipeStep.builder().recipe(recipe).sequence(4L).build()
        );
        recipeStepRepository.saveAll(recipeSteps);
        recipeStepRepository.increaseSequenceGte(recipe, 2L);
        List<RecipeStep> updated = recipeStepRepository.findAll();
        assertThat(updated.stream().mapToLong(RecipeStep::getSequence)).containsExactly(1L, 3L, 4L, 5L);

    }

    @After
    public void tearDown() throws Exception {
        recipeStepRepository.deleteAll();
        recipeRepository.deleteAll();
    }
}
