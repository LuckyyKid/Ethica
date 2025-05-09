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

    public String getMarketSummary(ClientPortfolio portfolio) {
        RestTemplate restTemplate = new RestTemplate();

        // Utiliser les bons HttpHeaders (de Spring, pas java.net)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Corps de la requête
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4.0");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a financial news analyst. You are not allowed to give investment advice."));
        messages.add(Map.of("role", "user", "content",
                "Donne-moi un résumé clair des actualités économiques des dernières 24h dans le monde, puis explique comment cela pourrait impacter un portefeuille qui détient : " + buildUserPortfolioSummary(portfolio)));

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Map.class);

        Map result = response.getBody();
        if (result != null && result.containsKey("choices")) {
            List choices = (List) result.get("choices");
            Map choice = (Map) choices.get(0);
            Map message = (Map) choice.get("message");
            return (String) message.get("content");
        }

        return "Désolé, une erreur est survenue.";
    }
    public String buildUserPortfolioSummary(ClientPortfolio portfolio) {
        var trades = tradingRepository.findByPortfolioOrderByTimestampAsc(portfolio);
        Map<String, Double> holdings = new HashMap<>();

        for (var trade : trades) {
            double qty = trade.getQuantity();
            String symbol = trade.getSymbol();

            if (trade.isBuy()) {
                holdings.put(symbol, holdings.getOrDefault(symbol, 0.0) + qty);
            } else {
                holdings.put(symbol, holdings.getOrDefault(symbol, 0.0) - qty);
            }
        }

        return holdings.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(e -> e.getKey() + " (" + e.getValue() + " actions)")
                .reduce((a, b) -> a + ", " + b)
                .orElse("aucune action actuellement");
    }

}
