package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthRequiredInterceptorTest {

    private AuthRequiredInterceptor authRequiredInterceptor;
    private HandlerMethod mockHandlerMethod;
    private MockHttpServletRequest mockHttpServletRequest;
    private MockHttpSession mockSession;

    @Before
    public void setUp() throws Exception {
        authRequiredInterceptor = new AuthRequiredInterceptor();
        mockHandlerMethod = mock(HandlerMethod.class);
        when(mockHandlerMethod.hasMethodAnnotation(AuthRequired.class)).thenReturn(true);
        mockHttpServletRequest = new MockHttpServletRequest();
        mockSession = new MockHttpSession();
        mockSession.putValue(SessionUtils.USER_SESSION_KEY, new User());
        mockHttpServletRequest.setSession(mockSession);
    }

    @Test
    public void notAHandlerMethod() throws Exception {
        assertTrue(authRequiredInterceptor.preHandle(mockHttpServletRequest, null, mock(ResourceHttpRequestHandler.class)));
    }

    @Test
    public void withNoAuthRequiredAnnotationAtMethod() throws Exception {
        authRequiredInterceptor = makeAnonymousInterceptor(true);

        assertTrue(authRequiredInterceptor.preHandle(mockHttpServletRequest, null, mockHandlerMethod));
    }

    @Test
    public void withNoAuthRequiredAnnotationAtClass() throws Exception {
        authRequiredInterceptor = makeAnonymousInterceptor(false);

        assertTrue(authRequiredInterceptor.preHandle(mockHttpServletRequest, null, mockHandlerMethod));
    }


    @Test(expected = UnauthorizedException.class)
    public void notLoggedIn() throws Exception {
        authRequiredInterceptor = makeAnonymousInterceptor(true);
        mockHttpServletRequest.setSession(new MockHttpSession());

        assertTrue(authRequiredInterceptor.preHandle(mockHttpServletRequest, null, mockHandlerMethod));
    }

    private AuthRequiredInterceptor makeAnonymousInterceptor(boolean isAnnotated) {
        return new AuthRequiredInterceptor() {
            @Override
            protected boolean isAnnotatedAuthRequiredAtClass(HandlerMethod handlerMethod) {
                return isAnnotated;
            }
        };
    }
}