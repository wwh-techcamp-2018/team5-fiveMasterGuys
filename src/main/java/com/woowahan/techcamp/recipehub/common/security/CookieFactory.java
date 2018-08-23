package com.woowahan.techcamp.recipehub.common.security;

import javax.servlet.http.Cookie;

public class CookieFactory {
    public static final String LOGGED_IN_KEY = "isLoggedIn";

    public static Cookie userLoginCookie() {
        Cookie cookie = new Cookie(LOGGED_IN_KEY, "true");
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie userLogoutCookie() {
        Cookie cookie = new Cookie("isLoggedIn", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
