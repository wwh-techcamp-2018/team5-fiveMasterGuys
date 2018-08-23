package com.woowahan.techcamp.recipehub.image.controller;

import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.common.support.RestResponse;
import com.woowahan.techcamp.recipehub.image.exception.FileUploadException;
import com.woowahan.techcamp.recipehub.image.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/images")
public class ImageUploadController {

    @Autowired
    private ImageStorageService imageStorageService;

    @PostMapping
    @AuthRequired
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws FileUploadException {
        return RestResponse.success(imageStorageService.store(file));
    }
}