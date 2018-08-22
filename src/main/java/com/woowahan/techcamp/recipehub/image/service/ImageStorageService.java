package com.woowahan.techcamp.recipehub.image.service;

import com.woowahan.techcamp.recipehub.common.exception.BadRequestException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class ImageStorageService {
    @Autowired
    private FileUploadService fileUploadService;

    private String bucketName;

    public String store(MultipartFile file) throws IOException {
        if (isInvalidImage(file)) {
            throw new BadRequestException();
        }

        String fileName = generateRandomFileName(file);
        File convertedFile = convertToFile(file).orElseThrow(IOException::new);
        String resultUrl = fileUploadService.putObject(bucketName, fileName, convertedFile);
        convertedFile.delete();
        return resultUrl;
    }

    private Optional<File> convertToFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        File convertFile = new File(filename);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private boolean isInvalidImage(MultipartFile file) {
        return file.isEmpty() || !file.getContentType().startsWith("image/");
    }

    private String generateRandomFileName(MultipartFile file) {
        return String.format("%d%s.%s", new Date().getTime(), RandomStringUtils.randomAlphabetic(6, 10), extractFileExtension(file));
    }

    private String extractFileExtension(MultipartFile file) {
        int dotIndex = file.getOriginalFilename().lastIndexOf(".");
        if (dotIndex == -1) {
            throw new BadRequestException();
        }
        return file.getOriginalFilename().substring(dotIndex + 1);
    }

    @Value("${cloud.aws.s3.bucket}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
