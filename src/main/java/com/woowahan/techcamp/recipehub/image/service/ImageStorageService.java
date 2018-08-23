package com.woowahan.techcamp.recipehub.image.service;

import com.woowahan.techcamp.recipehub.image.exception.FileUploadException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class ImageStorageService {
    @Autowired
    private FileUploadService fileUploadService;

    private String bucketName;

    public String store(MultipartFile file) throws FileUploadException {
        if (isInvalidImage(file)) {
            throw new FileUploadException();
        }

        try {
            return fileUploadService.putObject(bucketName, generateRandomFileName(file), file.getInputStream());
        } catch (IOException e) {
            throw new FileUploadException(e);
        }
    }


    private boolean isInvalidImage(MultipartFile file) {
        return file.isEmpty() || !file.getContentType().startsWith("image/");
    }

    private String generateRandomFileName(MultipartFile file) throws FileUploadException {
        return String.format("%d%s.%s", new Date().getTime(), RandomStringUtils.randomAlphabetic(6, 10), extractFileExtension(file));
    }

    private String extractFileExtension(MultipartFile file) throws FileUploadException {
        int dotIndex = file.getOriginalFilename().lastIndexOf(".");
        if (dotIndex == -1) {
            throw new FileUploadException();
        }
        return file.getOriginalFilename().substring(dotIndex + 1);
    }

    @Value("${cloud.aws.s3.bucket}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
