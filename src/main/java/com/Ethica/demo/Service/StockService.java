package com.Ethica.demo.Service;

import com.Ethica.demo.Entity.StockDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;

@Service
public class StockService {

    private static final Logger logger =
            LoggerFactory.getLogger(StockService.class);

    @Value("${finnhub.api.key}")
    private String apiKey;

    public StockDTO getStock(String ticker) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        logger.debug("Fetching stock data for {}", ticker);

        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + ticker + "&token=" + apiKey;
        String quoteResponse = restTemplate.getForObject(quoteUrl, String.class);
        JsonNode quoteJson = mapper.readTree(quoteResponse);

        double price = quoteJson.get("c").asDouble();
        double percentChange = quoteJson.get("dp").asDouble();

        String profileUrl = "https://finnhub.io/api/v1/stock/profile2?symbol=" + ticker + "&token=" + apiKey;
        String profileResponse = restTemplate.getForObject(profileUrl, String.class);
        JsonNode profileJson = mapper.readTree(profileResponse);

        String name = profileJson.get("name").asText();

        return new StockDTO(ticker, name, price, percentChange);
    }
}
