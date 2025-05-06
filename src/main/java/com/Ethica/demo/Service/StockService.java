package com.Ethica.demo.Service;

import com.Ethica.demo.Entity.StockDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

@Service
public class StockService {


    @Value("${finnhub.api.key}")
    private String apiKey;

    public StockDTO getStock(String ticker) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + ticker + "&token=" + apiKey;
        System.out.println("Clé API injectée : " + apiKey);


        String quoteResponce = restTemplate.getForObject(quoteUrl, String.class);
        JsonNode quoteJson = mapper.readTree(quoteResponce);

        double price = quoteJson.get("c").asDouble();
        double percentChange = quoteJson.get("dp").asDouble();

        // Appel pour le nom complet
        String profileUrl = "https://finnhub.io/api/v1/stock/profile2?symbol=" + ticker + "&token=" + apiKey;
        String profileResponse = restTemplate.getForObject(profileUrl, String.class);
        JsonNode profileJson = mapper.readTree(profileResponse);

        String name = profileJson.get("name").asText();

        // Retourner l'objet DTO construit à partir de ces valeurs
        return new StockDTO(ticker, name, price, percentChange);
    }
}





