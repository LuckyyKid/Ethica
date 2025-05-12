package com.Ethica.demo.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ScoringService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public List<Map<String, Object>> getStockRecommendations(String description, String profile, int age) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content",
                "You are a financial assistant. Based on the user's personality, investor profile, and age, suggest 5 stocks with their name, stock symbol, and a compatibility score (between 0 and 1). Return only a JSON array of objects like: [{name: 'Apple', symbol: 'AAPL', score: 0.92}, ...].Only reply with a valid JSON array. Do not write any explanation, header, markdown or text before or after."));

        String userInput = String.format("Description: %s\nInvestor Profile: %s\nAge: %d", description, profile, age);
        messages.add(Map.of("role", "user", "content", userInput));

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Map.class);
            Map result = response.getBody();
            if (result != null && result.containsKey("choices")) {
                List choices = (List) result.get("choices");
                Map choice = (Map) choices.get(0);
                Map message = (Map) choice.get("message");
                String content = (String) message.get("content");

                return new ObjectMapper().readValue(content, List.class);
            }
        } catch (Exception e) {
            System.out.println("AI API ERROR: " + e.getMessage());
        }

        return List.of();
    }
}
