package com.woowahan.techcamp.recipehub.image.controller;

import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.image.exception.InvalidFileException;
import com.woowahan.techcamp.recipehub.image.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping("/images")
public class ImageUploadController {

    @Autowired
    private ImageStorageService imageStorageService;

    @PostMapping
    @AuthRequired
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestResponse<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, InvalidFileException {
        return RestResponse.success(imageStorageService.store(file));
    }


    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestResponse<?> handleIOException(IOException exception) {
        return RestResponse.error("파일 저장 실패").build();
    }

    @ExceptionHandler(InvalidFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse<?> handleInvalidFileException(InvalidFileException exception) {
        return RestResponse.error("").build();
    }
}