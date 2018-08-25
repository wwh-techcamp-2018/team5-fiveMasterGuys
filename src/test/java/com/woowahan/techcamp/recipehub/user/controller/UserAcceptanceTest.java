package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {
    @Test
    public void signup() {
        ResponseEntity<String> response = requestGet("/users/signup");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Sign up");
    }

    @Test
    public void login() {
        ResponseEntity<String> response = requestGet("/users/login");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Login");
    }
}
