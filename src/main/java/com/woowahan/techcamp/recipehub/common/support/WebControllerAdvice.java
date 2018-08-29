package com.woowahan.techcamp.recipehub.common.support;

import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class WebControllerAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleError(BindException exception) {
        return "/support/badrequest";
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(BadRequestException exception) {
        return "/support/badrequest";
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleUnAuthorization(UnauthorizedException exception) {
        return new ModelAndView("redirect:/users/login");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(EntityNotFoundException exception) {
        return "/support/notfound";
    }
}
