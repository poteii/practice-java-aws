package org.example.aws.functions;

import org.example.aws.service.LambdaService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BookPriceCalculatorUI {
    private static LambdaService lambdaService;

    public static void main(String[] args) {
        // Load properties
        Properties properties = loadProperties();

        String accessKey = properties.getProperty("aws.accessKey");
        String secretKey = properties.getProperty("aws.secretKey");
        String region = properties.getProperty("aws.region");
        String functionName = properties.getProperty("lambda.functionName");

        lambdaService = new LambdaService(accessKey, secretKey, region);

        JFrame frame = new JFrame("Book Price Calculator");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, functionName);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, String functionName) {
        panel.setLayout(null);

        JLabel bookNameLabel = new JLabel("Book Name:");
        bookNameLabel.setBounds(10, 20, 80, 25);
        panel.add(bookNameLabel);

        JTextField bookNameText = new JTextField(20);
        bookNameText.setBounds(100, 20, 165, 25);
        panel.add(bookNameText);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(10, 50, 80, 25);
        panel.add(priceLabel);

        JTextField priceText = new JTextField(20);
        priceText.setBounds(100, 50, 165, 25);
        panel.add(priceText);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(10, 80, 150, 25);
        panel.add(calculateButton);

        JLabel resultLabel = new JLabel("Total Price:");
        resultLabel.setBounds(10, 110, 80, 25);
        panel.add(resultLabel);

        JTextField resultText = new JTextField(20);
        resultText.setBounds(100, 110, 165, 25);
        resultText.setEditable(false);
        panel.add(resultText);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = bookNameText.getText();
                double price = Double.parseDouble(priceText.getText());
                String payload = "{\"price\": " + price + "}";

                String response = lambdaService.invokeFunction(functionName, payload);
                resultText.setText(response);
            }// {"errorMessage":"Class not found: org.example.aws.functions.BookPriceCalculatorLambda","errorType":"java.lang.ClassNotFoundException"}
        });
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/aws-local.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}

