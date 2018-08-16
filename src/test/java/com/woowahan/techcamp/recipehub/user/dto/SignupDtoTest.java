package com.woowahan.techcamp.recipehub.user.dto;

import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class SignupDtoTest extends ValidationTest {
    private SignupDto.SignupDtoBuilder signupDtoBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        signupDtoBuilder = SignupDto.builder()
                .email("ming@woowahan.com")
                .name("ming")
                .password("abcdef123456")
                .passwordCheck("abcdef123456");
    }

    @Test
    public void valid() {
        assertConstraintViolations(signupDtoBuilder.build(), 0);
    }

    @Test
    public void invalid_email_pattern() {
        SignupDto dto = signupDtoBuilder
                .email("ming_woowahan.com")
                .build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void null_email() {
        SignupDto dto = signupDtoBuilder
                .email(null)
                .build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void only_integer_password() {
        assertConstraintViolations(setPairPasswordDto("1234567890"), 2);
    }

    @Test
    public void only_string_password() {
        assertConstraintViolations(setPairPasswordDto("abcdefghij"), 2);
    }

    @Test
    public void password_size_under_min() {
        assertConstraintViolations(setPairPasswordDto("1234567"), 2);
    }

    @Test
    public void password_size_over_max() {
        assertConstraintViolations(setPairPasswordDto("12345678abcdefghi"), 2);
    }

    @Test
    public void null_password() {
        assertConstraintViolations(setPairPasswordDto(null), 2);
    }

    @Test
    public void name_size_under_min() {
        SignupDto dto = signupDtoBuilder
                .name("a")
                .build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void name_size_over_max() {
        SignupDto dto = signupDtoBuilder
                .name(StringUtils.repeat("a", 46))
                .build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void null_name() {
        SignupDto dto = signupDtoBuilder
                .name(null)
                .build();
        assertConstraintViolations(dto, 1);
    }

    private SignupDto setPairPasswordDto(String password) {
        return signupDtoBuilder.password(password).passwordCheck(password).build();
    }
}
