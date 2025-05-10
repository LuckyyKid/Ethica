package com.Ethica.demo.Service;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Repo.TradingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {
    @Autowired
    private TradingRepository tradingRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String getMarketSummary(String userQuestion, String userPortfolioSummary) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content",
                "You're a financial analyst. You never give investment advice. You explain how recent economic events can impact a given portfolio.keep it short but detailed"));
        messages.add(Map.of("role", "user", "content",
                "Here are the user's latest transactions :\n" + userPortfolioSummary +
                        "\n\nHere's his question : " + userQuestion +
                        "\n\nClearly explains what has happened in the markets and the possible impact on its portfolio.keep it short but detailed"));

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Map.class);
            Map result = response.getBody();
            if (result != null && result.containsKey("choices")) {
                List choices = (List) result.get("choices");
                Map choice = (Map) choices.get(0);
                Map message = (Map) choice.get("message");
                return (String) message.get("content");
            }
        } catch (Exception e) {
            return "Error while calling  GPT : " + e.getMessage();
        }

        return "Response not available.";
    }


}
