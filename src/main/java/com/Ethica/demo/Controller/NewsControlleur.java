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
public class NewsControlleur {

    private static final String API_KEY = "eujbMLycKaoQbNXxe3uV1NHWhoybH3NLio9hV8j0";
    private static final String API_URL = "https://api.marketaux.com/v1/news/all";

    @GetMapping("/news")
    public String newsPage(
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            Model model) {

        List<Map<String, String>> articles = fetchNews(category);
        model.addAttribute("articles", articles);
        model.addAttribute("currentCategory", category);

        return "news";
    }

    private List<Map<String, String>> fetchNews(String category) {
        List<Map<String, String>> articles = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Build URL
            StringBuilder url = new StringBuilder(API_URL);
            url.append("?api_token=").append(API_KEY);
            url.append("&language=en");
            url.append("&limit=12");

            // Add category filter if not "all"
            if (category != null && !category.equals("all")) {
                url.append("&categories=").append(category);
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
                    articleMap.put("description", getTextValue(article, "description", getTextValue(article, "snippet", "No description available.")));
                    articleMap.put("url", getTextValue(article, "url", "#"));
                    articleMap.put("imageUrl", getTextValue(article, "image_url", ""));
                    articleMap.put("source", getTextValue(article, "source", "Unknown"));
                    articleMap.put("publishedAt", getTextValue(article, "published_at", ""));

                    // Get category
                    String cat = "General";
                    JsonNode entities = article.get("entities");
                    if (entities != null && entities.isArray() && entities.size() > 0) {
                        JsonNode firstEntity = entities.get(0);
                        String industry = getTextValue(firstEntity, "industry", "General");
                        if (!industry.equals("N/A")) {
                            cat = industry;
                        }
                    }
                    articleMap.put("category", cat);

                    articles.add(articleMap);
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching news: " + e.getMessage());
            e.printStackTrace();
        }

        return articles;
    }

    private String getTextValue(JsonNode node, String field, String defaultValue) {
        if (node == null) return defaultValue;
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) return defaultValue;
        return fieldNode.asText(defaultValue);
    }
}