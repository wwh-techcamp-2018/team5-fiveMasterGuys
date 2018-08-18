package com.woowahan.techcamp.recipehub.recipestep.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.techcamp.recipehub.common.exception.ParseException;

import java.io.IOException;
import java.util.List;

public class RecipeStepContentJsonConverter implements RecipeStepContentConverter {
    private ObjectMapper objectMapper;

    public RecipeStepContentJsonConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<String> toList(String content) {
        if (content == null) {
            return null;
        }

        try {
            return objectMapper.readValue(content, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            throw new ParseException();
        }
    }

    @Override
    public String toContentString(List<String> contents) {
        if (contents == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(contents);
        } catch (JsonProcessingException e) {
            throw new ParseException();
        }
    }
}
