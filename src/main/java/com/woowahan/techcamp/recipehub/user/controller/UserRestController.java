package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.common.security.CookieFactory;
import com.woowahan.techcamp.recipehub.common.security.SessionUtils;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.dto.LoginDTO;
import com.woowahan.techcamp.recipehub.user.dto.SignupDTO;
import com.woowahan.techcamp.recipehub.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse<User> signup(@Valid @RequestBody SignupDTO dto) {
        return RestResponse.success(userService.create(dto));
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginDTO dto, HttpSession session, HttpServletResponse response) {
        SessionUtils.addUserToSession(session, userService.login(dto));
        response.addCookie(CookieFactory.userLoginCookie());
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response, HttpSession session) {
        SessionUtils.removeUser(session);
        response.addCookie(CookieFactory.userLogoutCookie());
    }
}
