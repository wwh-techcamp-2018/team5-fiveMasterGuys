package com.woowahan.techcamp.recipehub.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class S3FileUploadService implements FileUploadService {

    @Autowired
    private AmazonS3Client client;

    @Override
    public String putObject(String bucketName, String fileName, File file) {
        client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return getUrl(bucketName, fileName);
    }

    private String getUrl(String bucketName, String fileName) {
        return client.getUrl(bucketName, fileName).toString();
    }
}
