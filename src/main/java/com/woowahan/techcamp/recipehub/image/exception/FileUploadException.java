package com.woowahan.techcamp.recipehub.image.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileUploadException extends Exception {
    public FileUploadException() {
    }

    public FileUploadException(Throwable cause) {
        super(cause);
    }
}
