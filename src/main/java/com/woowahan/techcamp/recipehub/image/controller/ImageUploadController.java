package com.woowahan.techcamp.recipehub.image.controller;

import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import com.woowahan.techcamp.recipehub.common.security.AuthRequired;
import com.woowahan.techcamp.recipehub.image.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/images")
public class ImageUploadController {

    @Autowired
    private ImageStorageService imageStorageService;

    @PostMapping
    @AuthRequired
    @ResponseStatus(HttpStatus.CREATED)
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            return imageStorageService.store(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException();
        }
    }
}