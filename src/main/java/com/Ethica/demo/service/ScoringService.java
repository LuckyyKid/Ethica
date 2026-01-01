package com.Ethica.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String CONTENT = "content";

    private static final Logger logger = LoggerFactory.getLogger(ScoringService.class);

    public List<Map<String, Object>> getStockRecommendations(String description, String profile, int age) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o");

        List<Map<String, String>> messages = new ArrayList<>();

        messages.add(Map.of(
                "role", "system",
                CONTENT,
                "You are a financial assistant. Return only a JSON array."
        ));

        String userInput = String.format(
                "Description: %s%nInvestor Profile: %s%nAge: %d",
                description, profile, age
        );

        messages.add(Map.of("role", "user", CONTENT, userInput));

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response =
                    restTemplate.exchange(API_URL, HttpMethod.POST, request, Map.class);

            Map<String, Object> result = response.getBody();

            if (result != null && result.containsKey("choices")) {
                List<Map<String, Object>> choices =
                        (List<Map<String, Object>>) result.get("choices");

                Map<String, Object> choice = choices.get(0);
                Map<String, Object> message =
                        (Map<String, Object>) choice.get("message");

                String content = (String) message.get(CONTENT);

                return new ObjectMapper().readValue(content, List.class);
            }

        } catch (Exception e) {
            logger.error("AI API error while fetching stock recommendations", e);
        }

        return List.of();
    }
}

