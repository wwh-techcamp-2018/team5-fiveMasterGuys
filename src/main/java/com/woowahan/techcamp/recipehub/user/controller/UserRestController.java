package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.common.security.SessionUtils;
import com.woowahan.techcamp.recipehub.user.dto.LoginDto;
import com.woowahan.techcamp.recipehub.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginDto dto, HttpSession session) {
        SessionUtils.addUserToSession(session, userService.login(dto));
    }
}
