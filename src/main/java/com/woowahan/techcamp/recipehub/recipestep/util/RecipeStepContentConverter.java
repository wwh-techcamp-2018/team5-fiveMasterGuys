package com.woowahan.techcamp.recipehub.recipestep.util;

import java.util.List;

public interface RecipeStepContentConverter {
    List toList(String content);

    String toContentString(List<String> contents);
}
