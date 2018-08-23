package com.woowahan.techcamp.recipehub.image.service;

import com.woowahan.techcamp.recipehub.image.exception.InvalidFileException;
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

    public String store(MultipartFile file) throws IOException, InvalidFileException {
        validateImage(file);

        return fileUploadService.putObject(bucketName, generateRandomFileName(file), file.getInputStream());
    }


    private void validateImage(MultipartFile file) throws InvalidFileException {
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new InvalidFileException();
        }
    }

    private String generateRandomFileName(MultipartFile file) throws InvalidFileException {
        return String.format("%d%s.%s",
                new Date().getTime(),
                RandomStringUtils.randomAlphabetic(6, 10),
                extractFileExtension(file));
    }

    private String extractFileExtension(MultipartFile file) throws InvalidFileException {
        int dotIndex = file.getOriginalFilename().lastIndexOf(".");
        if (dotIndex == -1) {
            throw new InvalidFileException();
        }
        return file.getOriginalFilename().substring(dotIndex + 1);
    }

    @Value("${cloud.aws.s3.bucket}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
