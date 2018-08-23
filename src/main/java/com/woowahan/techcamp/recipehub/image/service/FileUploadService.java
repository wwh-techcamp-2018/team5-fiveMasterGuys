package com.woowahan.techcamp.recipehub.image.service;

import com.woowahan.techcamp.recipehub.image.exception.FileUploadException;

import java.io.InputStream;

public interface FileUploadService {
    String putObject(String bucketName, String fileName, InputStream fileInputStream) throws FileUploadException;
}
