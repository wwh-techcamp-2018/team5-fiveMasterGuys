package com.woowahan.techcamp.recipehub.image.controller;

import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import com.woowahan.techcamp.recipehub.support.HtmlFormDataBuilder;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageUploadAcceptanceTest extends AcceptanceTest {
    @Test
    public void upload() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder
                .multipartFormData()
                .addParameter("file", new ClassPathResource("/static/img/hero-create.jpg"))
                .build();

        ResponseEntity<String> result = template(basicAuthUser).postForEntity("/images", request, String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(result.getBody()).startsWith("https://s3.ap-northeast-2.amazonaws.com/bucket/img/");

    }

    @Test
    public void uploadWithoutAuth() throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder
                .multipartFormData()
                .addParameter("file", new ClassPathResource("/static/img/hero-create.jpg"))
                .build();

        ResponseEntity<String> result = template().postForEntity("/images", request, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
