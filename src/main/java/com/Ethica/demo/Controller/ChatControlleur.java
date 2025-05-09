package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Repo.PortfolioRepository;
import com.Ethica.demo.Repo.UserRepository;
import com.Ethica.demo.Service.AiService;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")

public class ChatControlleur {
    @Autowired
    private AiService aiService;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        // Find the  User
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("response", "Utilisateur non authentifié."));
        }

        // Get his portfolio
        ClientPortfolio portfolio = portfolioRepository.findByUser(user).orElse(null);
        if (portfolio == null) {
            return ResponseEntity.status(404).body(Map.of("response", "Aucun portefeuille trouvé."));
        }

        String aiResponse = aiService.getMarketSummary(portfolio);

        Map<String, String> result = new HashMap<>();
        result.put("response", aiResponse);
        return ResponseEntity.ok(result);
    }
}
