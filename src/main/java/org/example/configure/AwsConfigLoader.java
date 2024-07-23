package org.example.configure;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AwsConfigLoader {
    private String accessKey;
    private String secretKey;
    private String region;

    public void loadAwsConfigurations(String environment) {
        Properties prop = new Properties();
        String fileName = "aws-" + environment + ".properties";

        try (FileInputStream input = new FileInputStream("path/to/" + fileName)) {
            prop.load(input);
            accessKey = prop.getProperty("aws.access.key");
            secretKey = prop.getProperty("aws.secret.key");
            region = prop.getProperty("aws.region");
        } catch (IOException ex) {
            System.err.println("Error loading AWS configuration for environment " + environment + ": " + ex.getMessage());
            return;
        }

        System.out.println("Environment: " + environment);
        System.out.println("AWS Access Key: " + accessKey);
        System.out.println("AWS Secret Key: " + secretKey);
        System.out.println("AWS Region: " + region);
    }

    public static void main(String[] args) {
        String environment = System.getProperty("environment", "local"); // Default to 'local' if not specified
        AwsConfigLoader configLoader = new AwsConfigLoader();
        configLoader.loadAwsConfigurations(environment);
    }
}
