package com.woowahan.techcamp.recipehub.step.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.techcamp.recipehub.step.exception.ConvertException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class StepContentConverter implements AttributeConverter<List, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List attribute) {

        if (attribute == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new ConvertException();
        }
    }

    @Override
    public List convertToEntityAttribute(String dbData) {

        if (dbData == null) {
            return null;
        }

        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (IOException e) {
            throw new ConvertException();
        }
    }
}
