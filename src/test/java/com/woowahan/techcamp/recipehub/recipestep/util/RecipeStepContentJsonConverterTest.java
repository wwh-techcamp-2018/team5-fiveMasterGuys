package com.woowahan.techcamp.recipehub.recipestep.util;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeStepContentJsonConverterTest {

    private RecipeStepContentConverter contentConverter;
    private List<String> contentList;
    private String contentString = "[\"식빵을 토스터기에 굽는다\",\"잼을 바른다\"]";

    @Before
    public void setUp() throws Exception {
        contentList = Arrays.asList("식빵을 토스터기에 굽는다", "잼을 바른다");
        contentConverter = new RecipeStepContentJsonConverter();
    }

    @Test
    public void toJsonArrayStringTest() {
        assertThat(contentConverter.toContentString(contentList))
                .isEqualTo(contentString);
    }

    @Test
    public void readValueTest() throws IOException {
        assertThat(contentConverter.toList(contentString))
                .isEqualTo(contentList);
    }

}