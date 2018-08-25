package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.dto.LoginDTO;
import com.woowahan.techcamp.recipehub.user.dto.SignupDTO;
import com.woowahan.techcamp.recipehub.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserRestAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    private static final String LOGIN_URL = "/api/users/login";
    private static final String SIGNUP_URL = "/api/users/signup";

    private SignupDTO.SignupDTOBuilder signupDTOBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        signupDTOBuilder = SignupDTO.builder()
                .email("test@test.com")
                .password("password1234")
                .passwordCheck("password1234")
                .name("tester");
    }

    @Test
    public void signup() {
        SignupDTO dto = signupDTOBuilder.build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getData().getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    public void signup_invalid_email() throws Exception {
        SignupDTO dto = signupDTOBuilder.email("test_test.com").build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signup_null_email() throws Exception {
        SignupDTO dto = signupDTOBuilder.email(null).build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signup_string_password() throws Exception {
        SignupDTO dto = signupDTOBuilder.password("qwerasdf").build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signup_number_password() throws Exception {
        SignupDTO dto = signupDTOBuilder.password("12345678").build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signup_password_under_min_size() throws Exception {
        SignupDTO dto = signupDTOBuilder.password("1234qwe").build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signup_password_over_max_size() throws Exception {
        SignupDTO dto = signupDTOBuilder.password("12345678qwertyuio").build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signup_not_match_password() throws Exception {
        SignupDTO dto = signupDTOBuilder
                .passwordCheck("password1234a").build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signup_name_under_min_size() throws Exception {
        SignupDTO dto = signupDTOBuilder.name("a").build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signup_name_over_max_size() throws Exception {
        SignupDTO dto = signupDTOBuilder.name(StringUtils.repeat("a", 41)).build();

        ResponseEntity<RestResponse<User>> response = responseSignup(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<RestResponse<User>> responseSignup(SignupDTO dto) {
        return requestJson(SIGNUP_URL, HttpMethod.POST, dto, new ParameterizedTypeReference<RestResponse<User>>() {
        });
    }

    @Test
    public void login() throws Exception {
        buildLoginRequest(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD)
                .andExpect(status().isOk());
    }

    @Test
    public void loginWithInvalidEmail() throws Exception {
        buildLoginRequest(DEFAULT_USER_EMAIL + "A", DEFAULT_USER_PASSWORD)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginWithInvalidPassword() throws Exception {
        buildLoginRequest(DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD + "A")
                .andExpect(status().isUnauthorized());
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
                .content(convertObjectToJsonBytes(new LoginDTO(email, password))));
    }
}
