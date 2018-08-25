package com.woowahan.techcamp.recipehub.image.service;

import com.woowahan.techcamp.recipehub.image.exception.FileAlreadyExistsException;
import com.woowahan.techcamp.recipehub.image.exception.FileNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalFileUploadServiceTest {
    private LocalFileUploadService localFileUploadService;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();


    private String imageStoragePath = "";
    private String tempFileName = "test.png";

    @Before
    public void setUp() throws Exception {
        localFileUploadService = new LocalFileUploadService(temporaryFolder.getRoot().getPath(), "");
    }

    @Test
    public void putObject() throws IOException {
        File tempFile = temporaryFolder.newFile();
        InputStream fis = new FileInputStream(tempFile);

        String resultUri = localFileUploadService.putObject("", tempFileName, fis);

        assertThat(temporaryFolder.getRoot().listFiles()).hasSize(2);
        assertThat(resultUri).isEqualTo(getFilePath(tempFileName));
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void putObjectAlreadyExist() throws IOException {
        File tempFile = temporaryFolder.newFile(tempFileName);
        InputStream fis = new FileInputStream(tempFile);

        localFileUploadService.putObject("", tempFileName, fis);
    }

    @Test
    public void loadAsResource() throws FileNotFoundException, IOException {
        File tempFile = temporaryFolder.newFile(tempFileName);
        Resource resource = localFileUploadService.loadAsResource(tempFileName);

        assertThat(resource.exists()).isTrue();
        assertThat(resource.getFilename()).isEqualTo(tempFileName);
    }

    @Test(expected = FileNotFoundException.class)
    public void loadAsResourceDoesNotExist() throws FileNotFoundException {
        localFileUploadService.loadAsResource("not_exist.png");
    }

    private String getFilePath(String fileName) {
        return String.format("%s/%s", imageStoragePath, fileName);
    }
}