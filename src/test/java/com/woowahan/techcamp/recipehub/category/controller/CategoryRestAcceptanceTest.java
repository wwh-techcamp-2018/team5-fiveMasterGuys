package com.woowahan.techcamp.recipehub.category.controller;

import com.woowahan.techcamp.recipehub.category.domain.Category;
import com.woowahan.techcamp.recipehub.category.repository.CategoryRepository;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryRestAcceptanceTest extends AcceptanceTest {

    @Autowired
    private CategoryRepository categoryRepository;
    private Category categoryA;
    private Category categoryB;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        categoryA = categoryRepository.save(new Category("A"));
        categoryB = categoryRepository.save(new Category("B"));
    }

    @Test
    public void readAllCategories() {
        ResponseEntity<RestResponse<List<Category>>> response = requestJson("/api/categories", HttpMethod.GET, new ParameterizedTypeReference<RestResponse<List<Category>>>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData()).contains(categoryA, categoryB);
    }
}
