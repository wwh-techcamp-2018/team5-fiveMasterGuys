package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import com.woowahan.techcamp.recipehub.user.domain.UserRepository;
import com.woowahan.techcamp.recipehub.user.dto.LoginDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    private static final String LOGIN_URL = "/api/users/login";

    @Test
    public void login() throws Exception {
        buildLoginRequest(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD)
                .andExpect(status().isOk());
    }

    @Test
    public void loginWithInvalidEmail() throws Exception {
        buildLoginRequest(DEFAULT_USER_EMAIL + "A", DEFAULT_USER_PASSWORD)
                .andExpect(status().isPermanentRedirect());
        //TODO: Move to UnAuthorization
    }

    @Test
    public void loginWithInvalidPassword() throws Exception {
        buildLoginRequest(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD + "A")
                .andExpect(status().isPermanentRedirect());
        //TODO: Move to UnAuthorization
    }

    @Test
    public void loginWithNullEmail() throws Exception {
        buildLoginRequest(null, DEFAULT_USER_PASSWORD)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginWithNullPassword() throws Exception {
        buildLoginRequest(DEFAULT_USER_EMAIL, null)
                .andExpect(status().isBadRequest());
    }

    private ResultActions buildLoginRequest(String email, String password) throws Exception {
        return mvc.perform(post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(new LoginDto(email, password))));
    }
}
