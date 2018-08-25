package com.woowahan.techcamp.recipehub.image.controller;

import com.woowahan.techcamp.recipehub.support.AcceptanceTest;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;

public class ImageUploadAcceptanceTest extends AcceptanceTest {

    @Test
    public void upload() throws Exception {
        MockMultipartFile mockPng = new MockMultipartFile("file", "test.png", "image/png", "asdf".getBytes());

        requestFileUpload(mockPng, basicAuthUser)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(containsString("https://s3.ap-northeast-2.amazonaws.com/bucket/img/")));
    }


    @Test
    public void uploadWithoutAuth() throws Exception {
        MockMultipartFile mockPng = new MockMultipartFile("file",
                "test.png",
                "image/png",
                "asdf".getBytes());

        requestFileUpload(mockPng)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void uploadWithoutFileExtension() throws Exception {
        MockMultipartFile mockPng = new MockMultipartFile("file",
                "test",
                "image/png",
                "asdf".getBytes());

        requestFileUpload(mockPng, basicAuthUser)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void uploadEmptyFile() throws Exception {
        MockMultipartFile mockPng = new MockMultipartFile("file",
                "test.png",
                "image/png",
                "".getBytes());

        requestFileUpload(mockPng, basicAuthUser)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void uploadWithWrongContentType() throws Exception {
        MockMultipartFile mockPng = new MockMultipartFile("file",
                "test.png",
                "text/html",
                "asdf".getBytes());

        requestFileUpload(mockPng, basicAuthUser)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
