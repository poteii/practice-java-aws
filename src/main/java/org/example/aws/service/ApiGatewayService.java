package org.example.aws.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Map;

public class ApiGatewayService {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String callApiGateway(String apiGatewayEndpoint, double price) throws Exception {
        String url = apiGatewayEndpoint;

        Map<String, Double> requestBody = Map.of("price", price);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        RequestBody body = RequestBody.create(
                jsonRequestBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
    }}
