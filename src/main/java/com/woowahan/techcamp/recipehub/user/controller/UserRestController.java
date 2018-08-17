package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.common.security.SessionUtils;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.dto.LoginDto;
import com.woowahan.techcamp.recipehub.user.dto.SignupDto;
import com.woowahan.techcamp.recipehub.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public User signup(@Valid @RequestBody SignupDto dto) {
        return userService.create(dto);
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginDto dto, HttpSession session) {
        SessionUtils.addUserToSession(session, userService.login(dto));
    }
}
