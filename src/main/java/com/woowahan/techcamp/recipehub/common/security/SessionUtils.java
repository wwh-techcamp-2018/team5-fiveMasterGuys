package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.user.domain.User;

import javax.servlet.http.HttpSession;

public class SessionUtils {
    private static final String USER_SESSION_KEY = "USER_SESSION_KEY";

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(USER_SESSION_KEY) != null;
    }

    public static void addUserToSession(HttpSession session, User user) {
        session.setAttribute(USER_SESSION_KEY, user);
    }
}
