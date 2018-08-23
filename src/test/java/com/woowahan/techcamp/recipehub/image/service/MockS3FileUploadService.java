package com.woowahan.techcamp.recipehub.image.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Profile("test")
public class MockS3FileUploadService implements FileUploadService {
    @Override
    public String putObject(String bucketName, String fileName, InputStream fileInputStream) {
        return String.format("https://s3.ap-northeast-2.amazonaws.com/%s/img/%s", bucketName, fileName);
    }
}
