package com.woowahan.techcamp.recipehub.user.dto;

import com.woowahan.techcamp.recipehub.support.ValidationTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class SignupDTOTest extends ValidationTest {
    private SignupDTO.SignupDTOBuilder signupDtoBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        signupDtoBuilder = SignupDTO.builder()
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
    public void invalidAll() {
        assertConstraintViolations(new SignupDTO(), 4);
    }

    @Test
    public void invalid_email_pattern() {
        SignupDTO dto = signupDtoBuilder
                .email("ming_woowahan.com")
                .build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void null_email() {
        SignupDTO dto = signupDtoBuilder
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
        SignupDTO dto = signupDtoBuilder
                .name("a")
                .build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void name_size_over_max() {
        SignupDTO dto = signupDtoBuilder
                .name(StringUtils.repeat("a", 41))
                .build();
        assertConstraintViolations(dto, 1);
    }

    @Test
    public void null_name() {
        SignupDTO dto = signupDtoBuilder
                .name(null)
                .build();
        assertConstraintViolations(dto, 1);
    }

    private SignupDTO setPairPasswordDto(String password) {
        return signupDtoBuilder.password(password).passwordCheck(password).build();
    }
}
