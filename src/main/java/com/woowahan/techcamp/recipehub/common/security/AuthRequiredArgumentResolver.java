package com.woowahan.techcamp.recipehub.common.security;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthRequiredArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(AuthRequired.class) && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return SessionUtils.getUserFromSession(webRequest).orElseThrow(UnauthorizedException::new);
    }
}
