package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.common.security.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping("/signup")
    public String signup(HttpSession session) {
        if (SessionUtils.isLoggedIn(session)) {
            return "redirect:/";
        }
        return "/users/signup";
    }

    @GetMapping("/login")
    public String userLogin(HttpSession session) {
        if (SessionUtils.isLoggedIn(session)) {
            return "redirect:/";
        }
        return "users/login";
    }

    @GetMapping("/logout")
    public String userLogout(HttpSession session) {
        SessionUtils.removeUser(session);
        return "redirect:/";
    }
}
