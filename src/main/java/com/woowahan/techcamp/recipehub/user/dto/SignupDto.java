package com.woowahan.techcamp.recipehub.user.dto;

import com.woowahan.techcamp.recipehub.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class SignupDto {
    @NotNull
    @Email(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotNull
    @Pattern(regexp = "^.*(?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z]).*$")
    private String password;

    @NotNull
    @Pattern(regexp = "^.*(?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z]).*$")
    private String passwordCheck;

    @NotNull
    @Size(min = 2, max = 45)
    private String name;

    @AssertTrue(message = "")
    private boolean matchPassword() {
        return password.equals(passwordCheck);
    }

    @Builder
    public SignupDto(String email, String password, String passwordCheck, String name) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.name = name;
    }

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .build();
    }
}
