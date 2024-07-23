package org.example.aws.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

public class LambdaService {
    private final LambdaClient lambdaClient;

    public LambdaService(String accessKey, String secretKey, String region) {
        AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(accessKey,secretKey);

        this.lambdaClient = LambdaClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
                .build();
    }

    public String invokeFunction(String functionName, String payload) {
        InvokeRequest invokeRequest = InvokeRequest.builder()
                .functionName(functionName)
                .payload(SdkBytes.fromUtf8String(payload))
                .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
        return invokeResponse.payload().asUtf8String();
    }


}
