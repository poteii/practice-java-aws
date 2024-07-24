package org.example;

import org.example.aws.functions.BookPriceCalculatorUI;
import org.example.aws.service.ApiGatewayService;
import org.example.aws.service.DynamoDBService;
import org.example.aws.service.LambdaService;
import org.example.aws.service.S3Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

public class Main {


    public static void main(String[] args) throws IOException {
        // Initialize AWS services
        // Load properties
        Properties properties = loadProperties();

        String accessKey = properties.getProperty("aws.accessKey");
        String secretKey = properties.getProperty("aws.secretKey");
        String region = properties.getProperty("aws.region");

        S3Service s3Service = new S3Service(accessKey, secretKey, region);
        // S3 upload and download
        String s3Key = s3Service.uploadFile(new FileInputStream("src/main/resources/test.txt"), 1024, "text/plain");
        System.out.println("Uploaded file to S3 with key: " + s3Key);

        DynamoDBService dynamoDBService = new DynamoDBService(accessKey, secretKey, region, properties.getProperty("dynamodb.tableName"));
        // DynamoDB put and get item
        String key = "UUID-" + UUID.randomUUID().toString();
        dynamoDBService.putItem(key, s3Key);
        System.out.println("Retrieved item from DynamoDB: " + dynamoDBService.getItem(key));

        // Interact with API Gateway via Lambda
        String apiGatewayEndpoint = properties.getProperty("api.gateway.endpoint");
        ApiGatewayService apiGatewayService = new ApiGatewayService();
        double price = 100.0;
        String lambdaResponse = null;
        try {
            lambdaResponse = apiGatewayService.callApiGateway(apiGatewayEndpoint, price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("API Gateway response: " + lambdaResponse);

        // Start UI
        BookPriceCalculatorUI.main(args);
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/aws-local.properties")) {
            properties.load(input);
            System.out.println("Loaded local properties file");
        } catch (IOException e) {
            try (InputStream input = new FileInputStream("src/main/resources/aws.properties")) {
                properties.load(input);
                System.out.println("Loaded default properties file");
            }
        }
        return properties;
    }
}