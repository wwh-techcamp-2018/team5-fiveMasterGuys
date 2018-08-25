package com.woowahan.techcamp.recipehub.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class S3FileUploadServiceTest {
    @Mock
    private AmazonS3Client client;

    @InjectMocks
    private S3FileUploadService s3FileUploadService;

    @Test
    public void putObject() throws IOException {
        String fileName = "test.png";
        MockMultipartFile file = new MockMultipartFile("asd", "asd", "image/png", "asd".getBytes());
        InputStream is = file.getInputStream();

        when(client.getUrl("", "img/" + fileName)).thenReturn(new URL("https://s3.ap-northeast-2.amazonaws.com/img/" + fileName));

        String result = s3FileUploadService.putObject("", fileName, is);

        verify(client).putObject(any(PutObjectRequest.class));
        assertThat(result).isEqualTo("https://s3.ap-northeast-2.amazonaws.com/img/" + fileName);
    }
}
