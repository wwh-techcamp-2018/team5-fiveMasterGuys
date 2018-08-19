package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtils {
    public static final String USER_SESSION_KEY = "USER_SESSION_KEY";

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(USER_SESSION_KEY) != null;
    }

    public static void addUserToSession(HttpSession session, User user) {
        session.setAttribute(USER_SESSION_KEY, user);
    }

    public static Optional<User> getUserFromSession(NativeWebRequest webRequest) {
        return Optional.ofNullable((User) webRequest.getAttribute(USER_SESSION_KEY, WebRequest.SCOPE_SESSION));
    }

    public static Optional<User> getUserFromSession(HttpSession session) {
        return Optional.ofNullable((User) session.getAttribute(USER_SESSION_KEY));
    }

    public static User removeUser(HttpSession session) {
        User user = getUserFromSession(session).get();
        session.removeAttribute(USER_SESSION_KEY);
        return user;
    }
}
