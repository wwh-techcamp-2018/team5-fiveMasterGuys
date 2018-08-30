package com.woowahan.techcamp.recipehub.step.dto;

import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StepCreationDTOTest extends ValidationTest {

    private StepCreationDTO.StepCreationDTOBuilder pizzaBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        pizzaBuilder = StepCreationDTO.builder()
                .name("Pizza");
    }

    @Test
    public void valid() {
        StepCreationDTO dto = pizzaBuilder.build();
        assertConstraintViolations(dto, 0);
    }

    @Test
    public void nameIsSpace() {
        StepCreationDTO dto = pizzaBuilder.name(" ").build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void nameIsEmpty() {
        StepCreationDTO dto = pizzaBuilder.name("").build();
        assertConstraintViolations(dto, 2);
    }

    @Test
    public void nameIsNull() {
        StepCreationDTO dto = pizzaBuilder.name(null).build();
        assertConstraintViolations(dto, 2);
    }

    @Test
    public void allConstraintViolations() {
        StepCreationDTO dto = StepCreationDTO.builder().build();
        assertConstraintViolations(dto, 2);
    }


    public static void assertDtoEqualToStep(StepCreationDTO dto, Step step) {
        assertThat(dto).isEqualToComparingOnlyGivenFields(
                step,
                "name", "content", "imgUrl");
    }
}
