package com.woowahan.techcamp.recipehub.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.InputStream;


@Service
@Profile({"production", "development"})
public class S3FileUploadService implements FileUploadService {
    private static final String IMG_DIR = "img/";

    @Autowired
    private AmazonS3Client client;

    @Override
    public String putObject(String bucketName, String fileName, InputStream is) {
        String filePath = IMG_DIR + fileName;
        client.putObject(new PutObjectRequest(bucketName, filePath, is, new ObjectMetadata())
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return getUrl(bucketName, filePath);
    }

    private String getUrl(String bucketName, String fileName) {
        return client.getUrl(bucketName, fileName).toString();
    }
}
