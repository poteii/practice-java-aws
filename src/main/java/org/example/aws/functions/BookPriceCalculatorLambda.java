package org.example.aws.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
// {"errorMessage":"Class not found: org.example.aws.functions.BookPriceCalculatorLambda","errorType":"java.lang.ClassNotFoundException"}
import java.util.Map;

public class BookPriceCalculatorLambda {

    public Map<String, Object> handleRequest(Map<String, Object> input) {
        try {
            Number priceNumber = (Number) input.get("price");
            double price = priceNumber.doubleValue();
            double tax = 0.10; // 10% tax
            double totalPrice = price + (price * tax);

            Map<String, Object> result = Map.of("totalPrice", totalPrice);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "Internal server error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        BookPriceCalculatorLambda handler = new BookPriceCalculatorLambda();
        ObjectMapper objectMapper = new ObjectMapper();

        // Sample input for testing
        String inputJson = "{\"price\": 100.0}";
        try {
            Map<String, Object> inputMap = objectMapper.readValue(inputJson, Map.class);
            Map<String, Object> result = handler.handleRequest(inputMap);
            String resultJson = objectMapper.writeValueAsString(result);
            System.out.println(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
