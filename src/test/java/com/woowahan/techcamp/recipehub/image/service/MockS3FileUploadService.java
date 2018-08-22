package com.woowahan.techcamp.recipehub.image.service;

import java.io.File;

public class MockS3FileUploadService implements FileUploadService {
    @Override
    public String putObject(String bucketName, String fileName, File file) {
        return String.format("https://s3.ap-northeast-2.amazonaws.com/%s/img/%s", bucketName, fileName);
    }
}
