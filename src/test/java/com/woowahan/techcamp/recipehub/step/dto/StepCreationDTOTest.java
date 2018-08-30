package com.woowahan.techcamp.recipehub.step.dto;

import com.woowahan.techcamp.recipehub.step.domain.Step;
import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class StepCreationDTOTest extends ValidationTest {

    private StepCreationDTO.StepCreationDTOBuilder pizzaBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        pizzaBuilder = StepCreationDTO.builder()
                .content(Arrays.asList("a"))
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
        StepCreationDTO dto = StepCreationDTO.builder()
                .content(Arrays.asList("1234567890123456789012345678901234567890123456", "b", "c", "d", "e", "f"))
                .build();
        assertConstraintViolations(dto, 4);
    }

    @Test
    public void contentMoreThan5() {
        StepCreationDTO dto = pizzaBuilder
                .content(Arrays.asList("a", "b", "c", "d", "e", "f"))
                .build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void contentItemLengthOver45() {
        StepCreationDTO dto = pizzaBuilder
                .content(Arrays.asList("1234567890123456789012345678901234567890123456"))
                .build();
        assertConstraintViolations(dto, 1);
    }

    public static void assertDtoEqualToStep(StepCreationDTO dto, Step step) {
        assertThat(dto).isEqualToComparingOnlyGivenFields(
                step,
                "name", "content", "imgUrl");
    }
}
