package com.woowahan.techcamp.recipehub.step.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StepTest {

    @Test
    public void close() {
        Step step = Step.builder().closed(false).build();
        step.close();
        assertThat(step.isClosed()).isTrue();
    }
}