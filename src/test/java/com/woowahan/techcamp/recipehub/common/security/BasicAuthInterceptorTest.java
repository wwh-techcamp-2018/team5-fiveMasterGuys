package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.dto.LoginDTO;
import com.woowahan.techcamp.recipehub.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicAuthInterceptorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private BasicAuthInterceptor basicAuthInterceptor;


    @Test
    public void preHandle_로그인_성공() throws Exception {
        final String email = "team5@recipehub.com";
        final String password = "password1234!";

        User user = User.builder().name("Username").build();

        LoginDTO dto = new LoginDTO(email, password);
        when(userService.login(dto)).thenReturn(user);

        //When
        String encodedBasicAuth = Base64.getEncoder()
                .encodeToString(String.format("%s:%s", email, password).getBytes());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic " + encodedBasicAuth);
        basicAuthInterceptor.preHandle(request, null, null);

        //Then
        assertTrue(SessionUtils.isLoggedIn(request.getSession()));
    }
}