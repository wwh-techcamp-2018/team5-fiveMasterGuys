package com.woowahan.techcamp.recipehub.user.service;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.domain.UserRepository;
import com.woowahan.techcamp.recipehub.user.dto.LoginDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void loginTest() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(USER_PASSWORD, null)).thenReturn(true);
        userService.login(new LoginDto(USER_EMAIL, USER_PASSWORD));
    }

    @Test(expected = UnauthorizedException.class)
    public void loginWithInvalidEmail() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        userService.login(new LoginDto(USER_EMAIL, USER_PASSWORD));
    }

    @Test(expected = UnauthorizedException.class)
    public void loginWithInvalidPassword() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(USER_PASSWORD, null)).thenReturn(false);
        userService.login(new LoginDto(USER_EMAIL, USER_PASSWORD));
    }
}
