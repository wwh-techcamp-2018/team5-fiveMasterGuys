package com.woowahan.techcamp.recipehub.image.service;

import com.woowahan.techcamp.recipehub.image.exception.FileNotFoundException;
import com.woowahan.techcamp.recipehub.image.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Profile("local")
@Service
public class LocalFileUploadService implements FileUploadService {
    @Value("${user.home}")
    private String userHomePath;

    @Value("${local.storage.imageStoragePath}")
    private String imageStoragePath;

    private String fullImageStoragePath;

    @PostConstruct
    private void init() throws IOException {
        fullImageStoragePath = userHomePath + imageStoragePath;
        Path rootDirPath = Paths.get(fullImageStoragePath);
        if (!Files.exists(rootDirPath)) {
            Files.createDirectories(rootDirPath);
        }
    }

    @Override
    public String putObject(String bucketName, String fileName, InputStream inputStream) throws FileUploadException {
        String filePath = String.format("%s/%s", fullImageStoragePath, fileName);
        File targetFile = new File(filePath);

        try {
            if (targetFile.createNewFile()) {
                writeFile(targetFile, inputStream);
                return String.format("%s/%s", imageStoragePath, fileName);
            }
            throw new FileUploadException();
        } catch (IOException e) {
            deleteFileIfExist(Paths.get(filePath));
            throw new FileUploadException(e);
        }
    }


    private void writeFile(File target, InputStream inputStream) throws IOException {
        try (InputStream is = inputStream; FileOutputStream fos = new FileOutputStream(target)) {
            IOUtils.copy(is, fos);
        }
    }

    private void deleteFileIfExist(Path path) throws FileUploadException {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileUploadException(e);
        }
    }

    public Resource loadAsResource(String filename) throws FileNotFoundException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException(e);
        }

        throw new FileNotFoundException();
    }

    public Path load(String filename) {
        return Paths.get(fullImageStoragePath).resolve(filename);
    }

}
