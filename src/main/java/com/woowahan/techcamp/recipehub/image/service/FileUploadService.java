package com.woowahan.techcamp.recipehub.image.service;

import java.io.IOException;
import java.io.InputStream;

public interface FileUploadService {
    String putObject(String bucketName, String fileName, InputStream fileInputStream) throws IOException;
}
