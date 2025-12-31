package com.Ethica.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StockNewsController {

    private static final String API_KEY = "a8vduqt5oket1g9ggk3lsahiunzgqjq2gwssen8s";
    private static final String API_BASE_URL = "https://stocknewsapi.com/api/v1";

    @GetMapping("/stock-news")
    public String stockNewsPage(
            @RequestParam(value = "ticker", required = false) String ticker,
            @RequestParam(value = "sentiment", required = false) String sentiment,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "source", required = false) String source,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "items", required = false, defaultValue = "20") int items,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Model model) {

        List<Map<String, String>> articles = new ArrayList<>();
        Map<String, Object> topMentioned = new HashMap<>();

        // Si un ticker est fourni, rechercher les news
        if (ticker != null && !ticker.isEmpty()) {
            articles = fetchTickerNews(ticker.toUpperCase(), sentiment, type, source, date, topic, items, page);
        }

        // Récupérer les top mentioned stocks
        topMentioned = fetchTopMentioned();

        model.addAttribute("articles", articles);
        model.addAttribute("topMentioned", topMentioned);
        model.addAttribute("currentTicker", ticker != null ? ticker.toUpperCase() : "");
        model.addAttribute("currentSentiment", sentiment != null ? sentiment : "");
        model.addAttribute("currentType", type != null ? type : "");
        model.addAttribute("currentSource", source != null ? source : "");
        model.addAttribute("currentDate", date != null ? date : "");
        model.addAttribute("currentTopic", topic != null ? topic : "");
        model.addAttribute("currentItems", items);
        model.addAttribute("currentPage", page);

        return "stockNews";
    }

    private List<Map<String, String>> fetchTickerNews(String ticker, String sentiment, String type,
                                                      String source, String date, String topic, int items, int page) {
        List<Map<String, String>> articles = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Build URL
            StringBuilder url = new StringBuilder(API_BASE_URL);
            url.append("?tickers=").append(ticker);
            url.append("&items=").append(Math.min(items, 50));
            url.append("&page=").append(page);
            url.append("&token=").append(API_KEY);

            // Add optional filters
            if (sentiment != null && !sentiment.isEmpty()) {
                url.append("&sentiment=").append(sentiment);
            }
            if (type != null && !type.isEmpty()) {
                url.append("&type=").append(type);
            }
            if (source != null && !source.isEmpty()) {
                url.append("&source=").append(source);
            }
            if (date != null && !date.isEmpty()) {
                url.append("&date=").append(date);
            }
            if (topic != null && !topic.isEmpty()) {
                url.append("&topic=").append(topic);
            }

            // Fetch data
            ResponseEntity<String> response = restTemplate.getForEntity(url.toString(), String.class);

            // Parse JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.get("data");

            if (data != null && data.isArray()) {
                for (JsonNode article : data) {
                    Map<String, String> articleMap = new HashMap<>();

                    articleMap.put("title", getTextValue(article, "title", "No title"));
                    articleMap.put("text", getTextValue(article, "text", ""));
                    articleMap.put("url", getTextValue(article, "news_url", "#"));
                    articleMap.put("imageUrl", getTextValue(article, "image_url", ""));
                    articleMap.put("source", getTextValue(article, "source_name", "Unknown"));
                    articleMap.put("date", getTextValue(article, "date", ""));
                    articleMap.put("sentiment", getTextValue(article, "sentiment", "Neutral"));
                    articleMap.put("type", getTextValue(article, "type", "Article"));

                    // Get tickers
                    JsonNode tickers = article.get("tickers");
                    if (tickers != null && tickers.isArray()) {
                        StringBuilder tickerStr = new StringBuilder();
                        for (JsonNode t : tickers) {
                            if (tickerStr.length() > 0) tickerStr.append(", ");
                            tickerStr.append(t.asText());
                        }
                        articleMap.put("tickers", tickerStr.toString());
                    } else {
                        articleMap.put("tickers", ticker);
                    }

                    articles.add(articleMap);
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching stock news: " + e.getMessage());
            e.printStackTrace();
        }

        return articles;
    }

    private Map<String, Object> fetchTopMentioned() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> stocks = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = API_BASE_URL + "/top-mention?token=" + API_KEY + "&date=last7days";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.get("data");

            if (data != null && data.isArray()) {
                int count = 0;
                for (JsonNode stock : data) {
                    if (count >= 10) break; // Limit to top 10

                    Map<String, String> stockMap = new HashMap<>();
                    stockMap.put("ticker", getTextValue(stock, "ticker", ""));
                    stockMap.put("name", getTextValue(stock, "name", ""));
                    stockMap.put("mentions", getTextValue(stock, "mentions", "0"));
                    stocks.add(stockMap);
                    count++;
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching top mentioned: " + e.getMessage());
        }

        result.put("stocks", stocks);
        return result;
    }

    private String getTextValue(JsonNode node, String field, String defaultValue) {
        if (node == null) return defaultValue;
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) return defaultValue;
        return fieldNode.asText(defaultValue);
    }
}