package org.example.aws.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBService {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public DynamoDBService(String accessKey, String secretKey, String region, String tableName) {
        AwsBasicCredentials basicCredentials = AwsBasicCredentials.create(accessKey,secretKey);
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
                .build();
        this.tableName = tableName;
    }

    public void putItem(String key, String value) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(key).build());
        item.put("value", AttributeValue.builder().s(value).build());

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(this.tableName)
                .item(item)
                .build());
    }

    public Map<String, AttributeValue> getItem(String key) {
        Map<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("id", AttributeValue.builder().s(key).build());

        return dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(this.tableName)
                .key(keyToGet)
                .build()).item();
    }
}
