package com.woowahan.techcamp.recipehub.user.dto;

import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.junit.Test;

public class LoginDTOTest extends ValidationTest {

    private static final String EMAIL = "team5@recipehub.com";
    private static final String PASSWORD = "password1234!";

    @Test
    public void valid() {
        assertConstraintViolations(new LoginDTO(EMAIL, PASSWORD), 0);
    }

    @Test
    public void withNullEmail() {
        assertConstraintViolations(new LoginDTO(null, PASSWORD), 1);
    }

    @Test
    public void withNullPassword() {
        assertConstraintViolations(new LoginDTO(EMAIL, null), 1);
    }

    @Test
    public void emailMismatchPattern() {
        assertConstraintViolations(new LoginDTO("not-a-pattern", PASSWORD), 1);
    }

    @Test
    public void passwordMismatchPattern() {
        assertConstraintViolations(new LoginDTO(EMAIL, "not-a-pattern"), 1);
    }
}
