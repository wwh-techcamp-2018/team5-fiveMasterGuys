package com.woowahan.techcamp.recipehub.image.controller;

import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.common.support.Message;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.image.exception.InvalidFileException;
import com.woowahan.techcamp.recipehub.image.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;


@RestController
@RequestMapping("/images")
public class ImageUploadController {

    private final int imageMaxSize = 5 * 1024 * 1024;

    @Autowired
    private ImageStorageService imageStorageService;

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor messageSourceAccessor;

    @PostMapping
    @AuthRequired
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestResponse<String> handleFileUpload(@RequestPart MultipartFile file) throws IOException, InvalidFileException {
        if (file.getSize() > imageMaxSize) {
            throw new InvalidFileException(messageSourceAccessor.getMessage(Message.IMAGE_SIZE_EXCEEDED));
        }

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
        return RestResponse.error(exception.getMessage()).build();
    }
}