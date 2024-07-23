package org.example.aws.service;



import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.UUID;

public class S3Service {

    private final AmazonS3 s3Client;
    private final String BUCKET_NAME = "simpleitem";

    public S3Service(String accessKey, String secretKey, Regions region) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public String uploadFile(InputStream inputStream, long contentLength, String contentType) {
        String key = UUID.randomUUID().toString();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);
        s3Client.putObject(BUCKET_NAME, key, inputStream, metadata);
        return key;
    }

    public S3Object downloadFile(String key) {
        return s3Client.getObject(BUCKET_NAME, key);
    }
}

