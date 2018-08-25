package com.woowahan.techcamp.recipehub.user.service;

import com.woowahan.techcamp.recipehub.common.exception.ResourceExistsException;
import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.dto.LoginDTO;
import com.woowahan.techcamp.recipehub.user.dto.SignupDTO;
import com.woowahan.techcamp.recipehub.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private SignupDTO signupDto;

    @Before
    public void setUp() throws Exception {
        signupDto = SignupDTO.builder()
                .email("ming@woowahan.com")
                .name("ming")
                .password("abcdef123456")
                .passwordCheck("abcdef123456")
                .build();
    }

    @Test
    public void create() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User user = signupDto.toEntity(passwordEncoder);

        when(userRepository.save(user)).thenReturn(user);
        assertThat(userService.create(signupDto)).isEqualTo(user);
    }

    @Test(expected = ResourceExistsException.class)
    public void duplicatedEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        signupDto.setEmail("ming@woowahan.com");
        userService.create(signupDto);
    }

    @Test
    public void loginTest() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(USER_PASSWORD, null)).thenReturn(true);
        userService.login(new LoginDTO(USER_EMAIL, USER_PASSWORD));
    }

    @Test(expected = UnauthorizedException.class)
    public void loginWithInvalidEmail() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        userService.login(new LoginDTO(USER_EMAIL, USER_PASSWORD));
    }

    @Test(expected = UnauthorizedException.class)
    public void loginWithInvalidPassword() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(USER_PASSWORD, null)).thenReturn(false);
        userService.login(new LoginDTO(USER_EMAIL, USER_PASSWORD));
    }
}
