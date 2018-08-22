package com.woowahan.techcamp.recipehub.image.service;

import java.io.File;

public interface FileUploadService {
    String putObject(String bucketName, String fileName, File file);
}
