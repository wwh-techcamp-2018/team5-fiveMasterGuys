package com.woowahan.techcamp.recipehub.recipe.controller;

import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeAcceptanceTest extends AcceptanceTest {

    @Test
    public void create() throws Exception {
        mvc.perform(post("/api/recipe")).andExpect(status().isCreated());
    }
}
