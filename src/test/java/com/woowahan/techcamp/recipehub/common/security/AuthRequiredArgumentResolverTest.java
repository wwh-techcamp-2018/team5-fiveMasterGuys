package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AuthRequiredArgumentResolverTest {

    @Mock
    private MethodParameter parameter;

    @Mock
    private NativeWebRequest request;

    @Mock
    private AuthRequired authRequiredAnnotation;

    private AuthRequiredArgumentResolver resolver;
    private User user;
    private User adminUser;

    @Before
    public void setup() {
        resolver = new AuthRequiredArgumentResolver();
        user = User.builder().name("username").build();
        when(parameter.getParameterAnnotation(AuthRequired.class)).thenReturn(authRequiredAnnotation);
    }

    @Test(expected = UnauthorizedException.class)
    public void notLoggedInUser() throws Exception {
        when(request.getAttribute(SessionUtils.USER_SESSION_KEY, WebRequest.SCOPE_SESSION)).thenReturn(null);

        resolver.resolveArgument(parameter, null, request, null);
    }

    @Test
    public void loggedInUser() throws Exception {
        when(request.getAttribute(SessionUtils.USER_SESSION_KEY, WebRequest.SCOPE_SESSION)).thenReturn(user);

        assertThat(((User) resolver.resolveArgument(parameter, null, request, null)).getName())
                .isEqualTo(user.getName());
    }

    @Test
    public void parameterTypeIsNotUser() {
        when(parameter.hasMethodAnnotation(AuthRequired.class)).thenReturn(true);
        when(parameter.getParameterType()).thenAnswer((Answer<Object>) invocation -> Object.class);
        assertFalse(resolver.supportsParameter(parameter));
    }

    @Test
    public void parameterDoesntHaveAuthRequired() {
        when(parameter.hasMethodAnnotation(AuthRequired.class)).thenReturn(false);
        assertFalse(resolver.supportsParameter(parameter));
    }

    @Test
    public void parameterShouldBeSupported() {
        when(parameter.hasMethodAnnotation(AuthRequired.class)).thenReturn(true);
        when(parameter.getParameterType()).thenAnswer((Answer<Object>) invocation -> User.class);
        assertTrue(resolver.supportsParameter(parameter));
    }
}