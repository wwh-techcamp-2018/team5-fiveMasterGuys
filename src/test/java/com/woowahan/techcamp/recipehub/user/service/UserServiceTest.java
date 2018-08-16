package com.woowahan.techcamp.recipehub.user.service;

import com.woowahan.techcamp.recipehub.exception.ConflictException;
import com.woowahan.techcamp.recipehub.user.Service.UserService;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.domain.UserRepository;
import com.woowahan.techcamp.recipehub.user.dto.SignupDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private SignupDto signupDto;

    @Before
    public void setUp() throws Exception {
        signupDto = SignupDto.builder()
                .email("ming@woowahan.com")
                .name("ming")
                .password("abcdef123456")
                .passwordCheck("abcdef123456")
                .build();
    }

    @Test
    public void add() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User user = signupDto.toEntity(passwordEncoder);

        when(userRepository.save(user)).thenReturn(user);
        assertThat(userService.add(signupDto)).isEqualTo(user);
    }

    @Test(expected = ConflictException.class)
    public void duplicatedEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        signupDto.setEmail("ming@woowahan.com");
        userService.add(signupDto);
    }
}
