package com.woowahan.techcamp.recipehub.common.security;

import org.junit.Test;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieFactoryTest {

    @Test
    public void userLoginCookie() {
        Cookie cookie = CookieFactory.userLoginCookie();
        assertThat(cookie.getName()).isEqualTo(CookieFactory.LOGGED_IN_KEY);
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getValue()).isEqualTo("true");
    }

    @Test
    public void userLogoutCookie() {
        Cookie cookie = CookieFactory.userLogoutCookie();
        assertThat(cookie.getName()).isEqualTo(CookieFactory.LOGGED_IN_KEY);
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getValue()).isEqualTo("");
        assertThat(cookie.getMaxAge()).isEqualTo(0);
    }
}