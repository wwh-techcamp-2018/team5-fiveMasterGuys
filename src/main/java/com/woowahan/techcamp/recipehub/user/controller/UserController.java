package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.common.security.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @GetMapping("/users/login")
    public String userLogin(HttpSession session) {
        if (SessionUtils.isLoggedIn(session)) {
            return "index";
        }
        return "users/login";
    }
}
