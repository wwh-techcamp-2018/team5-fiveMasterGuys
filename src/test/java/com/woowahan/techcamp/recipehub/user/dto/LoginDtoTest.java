package com.woowahan.techcamp.recipehub.user.dto;

import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.junit.Test;

public class LoginDtoTest extends ValidationTest {

    private static final String EMAIL = "team5@recipehub.com";
    private static final String PASSWORD = "password1234!";

    @Test
    public void valid() {
        assertConstraintViolations(new LoginDto(EMAIL, PASSWORD), 0);
    }

    @Test
    public void withNullEmail() {
        assertConstraintViolations(new LoginDto(null, PASSWORD), 1);
    }

    @Test
    public void withNullPassword() {
        assertConstraintViolations(new LoginDto(EMAIL, null), 1);
    }

    @Test
    public void emailMismatchPattern() {
        assertConstraintViolations(new LoginDto("not-a-pattern", PASSWORD), 1);
    }

    @Test
    public void passwordMismatchPattern() {
        assertConstraintViolations(new LoginDto(EMAIL, "not-a-pattern"), 1);
    }
}
