package org.example.aws.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;

public class S3Service {

    private final S3Client s3Client;
    private final String BUCKET_NAME = "simpleitem";

    public S3Service(String accessKey, String secretKey, String region) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public String uploadFile(InputStream inputStream, long contentLength, String contentType) {
        String key = UUID.randomUUID().toString();
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build(), RequestBody.fromInputStream(inputStream, contentLength));
        return key;
    }

    public void downloadFile(String key, String destinationPath) {
        s3Client.getObject(GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build(), Paths.get(destinationPath));
    }
}

