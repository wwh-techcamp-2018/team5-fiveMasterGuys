package com.woowahan.techcamp.recipehub.user.controller;

import com.woowahan.techcamp.recipehub.user.Service.UserService;
import com.woowahan.techcamp.recipehub.user.dto.SignupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupDto dto) {
        userService.add(dto);
    }
}
