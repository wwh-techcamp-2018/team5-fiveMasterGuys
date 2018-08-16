package com.woowahan.techcamp.recipehub.user.domain;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertTrue;

public class UserTest {
    private final String USER_PASSWORD = "USER_PASSWORD";


    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void passwordMatch() {
        assertTrue(User.builder().password(passwordEncoder.encode(USER_PASSWORD)).build()
                .matchPassword(USER_PASSWORD, passwordEncoder));
    }
}
