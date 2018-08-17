package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthRequiredInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.hasMethodAnnotation(AuthRequired.class) && !handlerMethod.getMethod().getDeclaringClass().isAnnotationPresent(AuthRequired.class)) {
            return true;
        }

        if (SessionUtils.isLoggedIn(request.getSession())) {
            return true;
        }

        throw new UnauthorizedException();
    }
}
