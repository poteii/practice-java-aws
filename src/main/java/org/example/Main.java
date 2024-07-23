package org.example;

import com.amazonaws.regions.Regions;
import org.example.aws.service.S3Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        // Initialize AWS services
        // Load properties
        Properties properties = loadProperties();

        String accessKey = properties.getProperty("aws.accessKey");
        String secretKey = properties.getProperty("aws.secretKey");
        Regions region = Regions.fromName(properties.getProperty("aws.region"));

        S3Service s3Service = new S3Service(accessKey, secretKey, region);
        // S3 upload and download
        String s3Key = s3Service.uploadFile(new FileInputStream("src/main/resources/test.txt"), 1024, "text/plain");
        System.out.println("Uploaded file to S3 with key: " + s3Key);
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