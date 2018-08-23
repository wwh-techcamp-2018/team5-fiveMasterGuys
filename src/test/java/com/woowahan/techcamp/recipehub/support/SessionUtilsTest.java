package com.woowahan.techcamp.recipehub.support;

import com.woowahan.techcamp.recipehub.common.security.SessionUtils;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

import static com.woowahan.techcamp.recipehub.common.security.SessionUtils.USER_SESSION_KEY;
import static com.woowahan.techcamp.recipehub.common.security.SessionUtils.getUserFromSession;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class SessionUtilsTest {

    private User user;

    @Before
    public void setUp() throws Exception {
        user = User.builder().build();
    }

    @Test
    public void isLoggedIn() throws Exception {

        MockHttpSession session = new MockHttpSession();
        assertFalse(SessionUtils.isLoggedIn(session));

        session.setAttribute(USER_SESSION_KEY, user);
        assertTrue(SessionUtils.isLoggedIn(session));
    }

    @Test
    public void addUser() throws Exception {
        MockHttpSession session = new MockHttpSession();

        assertFalse(SessionUtils.getUserFromSession(session).isPresent());
        SessionUtils.addUserToSession(session, user);
        assertThat(session.getAttribute(USER_SESSION_KEY)).isEqualTo(user);

    }

    @Test
    public void getUser() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(USER_SESSION_KEY, user);
        assertThat(SessionUtils.getUserFromSession(session).get()).isEqualTo(user);

    }

    @Test
    public void addAndGet() throws Exception {
        MockHttpSession session = new MockHttpSession();

        assertFalse(SessionUtils.getUserFromSession(session).isPresent());
        SessionUtils.addUserToSession(session, user);
        assertThat(SessionUtils.getUserFromSession(session).get()).isEqualTo(user);

    }

    @Test
    public void removeUserFromSession() throws Exception {
        MockHttpSession session = new MockHttpSession();
        assertFalse(SessionUtils.getUserFromSession(session).isPresent());
        SessionUtils.addUserToSession(session, user);

        User removed = SessionUtils.removeUser(session);
        assertFalse(SessionUtils.getUserFromSession(session).isPresent());
        assertThat(removed).isEqualTo(user);
    }

    @Test
    public void webRequestWithScopeSession() throws Exception {
        ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest());
        webRequest.setAttribute(USER_SESSION_KEY, user, WebRequest.SCOPE_SESSION);
        assertThat(getUserFromSession(webRequest).get()).isEqualTo(user);
    }

    @Test(expected = NoSuchElementException.class)
    public void webRequestWithRequestSession() throws Exception {
        ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest());
        webRequest.setAttribute(USER_SESSION_KEY, user, WebRequest.SCOPE_REQUEST);
        getUserFromSession(webRequest).get();
    }
}
