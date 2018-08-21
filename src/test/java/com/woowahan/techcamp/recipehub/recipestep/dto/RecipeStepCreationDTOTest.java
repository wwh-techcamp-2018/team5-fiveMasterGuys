package com.woowahan.techcamp.recipehub.recipestep.dto;

import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.junit.Before;
import org.junit.Test;

public class RecipeStepCreationDTOTest extends ValidationTest {

    private RecipeStepCreationDTO.RecipeStepCreationDTOBuilder pizzaBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        pizzaBuilder = RecipeStepCreationDTO.builder()
                .recipeId(1L).name("Pizza");
    }

    @Test
    public void valid() {
        RecipeStepCreationDTO dto = pizzaBuilder.build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void nameIsSpace() {
        RecipeStepCreationDTO dto = pizzaBuilder.name(" ").build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void nameIsEmpty() {
        RecipeStepCreationDTO dto = pizzaBuilder.name("").build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void nameIsNull() {
        RecipeStepCreationDTO dto = pizzaBuilder.name(null).build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void recipeIdIsNull() {
        RecipeStepCreationDTO dto = pizzaBuilder.recipeId(null).build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void allConstraintViolations() {
        RecipeStepCreationDTO dto = RecipeStepCreationDTO.builder().build();
        assertConstraintViolations(dto, 2);
    }
}
