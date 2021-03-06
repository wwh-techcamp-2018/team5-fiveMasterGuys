package com.woowahan.techcamp.recipehub.image.service;

import com.woowahan.techcamp.recipehub.image.exception.FileAlreadyExistsException;
import com.woowahan.techcamp.recipehub.image.exception.FileNotFoundException;
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

    private String userHomePath;

    private String imageStoragePath;

    private String fullImageStoragePath;

    public LocalFileUploadService(@Value("${user.home}") String userHomePath,
                                  @Value("${local.storage.imageStoragePath}") String imageStoragePath) {
        this.userHomePath = userHomePath;
        this.imageStoragePath = imageStoragePath;

        fullImageStoragePath = userHomePath + imageStoragePath;
    }

    @PostConstruct
    private void init() throws IOException {
        Path rootDirPath = Paths.get(fullImageStoragePath);
        if (!Files.exists(rootDirPath)) {
            Files.createDirectories(rootDirPath);
        }
    }

    @Override
    public String putObject(String bucketName, String fileName, InputStream inputStream) throws IOException {
        String filePath = getFilePath(fullImageStoragePath, fileName);
        File targetFile = new File(filePath);

        if (targetFile.createNewFile()) {
            writeFile(targetFile, inputStream);
            return getFilePath(imageStoragePath, fileName);
        }

        throw new FileAlreadyExistsException();
    }

    private String getFilePath(String path, String fileName) {
        return String.format("%s/%s", path, fileName);
    }

    private void writeFile(File target, InputStream inputStream) throws IOException {
        try (InputStream is = inputStream; FileOutputStream fos = new FileOutputStream(target)) {
            IOUtils.copy(is, fos);
        } catch (IOException e) {
            Files.deleteIfExists(Paths.get(target.getName()));
            throw e;
        }
    }

    public Resource loadAsResource(String filename) throws FileNotFoundException {
        Path file = resolvePath(filename);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new FileNotFoundException(e);
        }

        if (resource.exists() || resource.isReadable()) {
            return resource;
        }

        throw new FileNotFoundException();
    }

    private Path resolvePath(String filename) {
        return Paths.get(fullImageStoragePath).resolve(filename);
    }
}
