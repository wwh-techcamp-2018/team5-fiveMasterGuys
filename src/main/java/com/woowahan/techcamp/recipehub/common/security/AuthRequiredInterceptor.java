package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthRequiredInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.hasMethodAnnotation(AuthRequired.class)
                && !isAnnotatedAuthRequiredAtClass(handlerMethod)) {
            return true;
        }

        if (SessionUtils.isLoggedIn(request.getSession())) {
            return true;
        }

        throw new UnauthorizedException();
    }

    protected boolean isAnnotatedAuthRequiredAtClass(HandlerMethod handlerMethod) {
        return handlerMethod.getMethod().getDeclaringClass().isAnnotationPresent(AuthRequired.class);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        SessionUtils.getUserFromSession(request.getSession()).ifPresent(user -> {
            if (modelAndView != null) modelAndView.addObject("user", user);
        });

    }
}
