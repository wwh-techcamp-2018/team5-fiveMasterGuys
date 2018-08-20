package com.woowahan.techcamp.recipehub.recipestep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConvertException extends RuntimeException {
}
