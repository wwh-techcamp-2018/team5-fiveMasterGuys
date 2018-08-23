package com.woowahan.techcamp.recipehub.image.service;

import com.woowahan.techcamp.recipehub.image.exception.InvalidFileException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ImageStorageServiceTest {
    private static final String BUCKET_NAME = "recipehub-image";

    @Spy
    private FileUploadService client = new MockS3FileUploadService();

    @InjectMocks
    private ImageStorageService service;


    @Before
    public void setUp() throws Exception {
        service.setBucketName(BUCKET_NAME);
    }

    @Test
    public void save() throws InvalidFileException, IOException {
        String name = "image.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", name, "image/png", "asdf".getBytes());

        String resultUri = service.store(mockMultipartFile);

        assertThat(resultUri).startsWith("https://s3.ap-northeast-2.amazonaws.com/" + BUCKET_NAME + "/img/");
    }
}