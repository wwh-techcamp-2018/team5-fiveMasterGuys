package com.woowahan.techcamp.recipehub.step.converter;

import com.woowahan.techcamp.recipehub.step.exception.ConvertException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StepContentConverterTest {

    private StepContentConverter contentConverter;
    private List<String> contentList;
    private String contentString = "[\"식빵을 토스터기에 굽는다\",\"잼을 바른다\"]";

    @Before
    public void setUp() throws Exception {
        contentList = Arrays.asList("식빵을 토스터기에 굽는다", "잼을 바른다");
        contentConverter = new StepContentConverter();
    }

    @Test
    public void convertToDatabaseColumn() {
        assertThat(contentConverter.convertToDatabaseColumn(contentList))
                .isEqualTo(contentString);
    }

    @Test
    public void convertToEntityAttribute() {
        assertThat(contentConverter.convertToEntityAttribute(contentString))
                .isEqualTo(contentList);
    }

    @Test(expected = ConvertException.class)
    public void wrongToEntityAttribute() throws Exception {
        contentConverter.convertToEntityAttribute("sdfslkdfhjsd");

    }
}