package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.dto.LoginDTO;
import com.woowahan.techcamp.recipehub.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Base64;

@Slf4j
public class BasicAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    private final String basicAuthPrefix = "Basic";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String authorizationHeader = request.getHeader("Authorization");
        log.debug("Authorization Header String: {}", authorizationHeader);
        if (authorizationHeader == null || !authorizationHeader.startsWith(basicAuthPrefix)) {
            return true;
        }

        String base64Credentials = authorizationHeader.substring(basicAuthPrefix.length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));

        final String[] values = credentials.split(":", 2);
        log.debug("Email    : {}", values[0]);
        log.debug("Password : {}", values[1]);

        try {
            User user = userService.login(new LoginDTO(values[0], values[1]));
            log.debug("Login Succeed with name  : ", user.getName());
            SessionUtils.addUserToSession(request.getSession(), user);
        } catch (UnauthorizedException unauthorizedException) {
            log.debug("Login Failed");
        } finally {
            return true;
        }
    }

}
