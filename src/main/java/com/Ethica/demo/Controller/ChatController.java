package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Repo.PortfolioRepository;
import com.Ethica.demo.Repo.TradingRepository;
import com.Ethica.demo.Service.AiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class ChatController {

    @Autowired
    private AiService aiService;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TradingRepository tradingRepository;


    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> payload, HttpSession session) {
        String userQuestion = payload.get("message");

        // User session
        User currentUser = (User) session.getAttribute("userConnecte");
        if (currentUser == null) {
            return ResponseEntity.status(401).body(Map.of("response", "User is not connected."));
        }

        // portfolio
        ClientPortfolio portfolio = portfolioRepository.findByUser(currentUser).orElse(null);
        if (portfolio == null) {
            return ResponseEntity.status(404).body(Map.of("response", "No portfolio found."));
        }

        // trades
        List<Trade> trades = tradingRepository.findByPortfolioOrderByTimestampAsc(portfolio);
        if (trades.isEmpty()) {
            return ResponseEntity.ok(Map.of("response", "No transactions found to analyze the portfolio."));
        }

        // Generate the message to send to GPT
        StringBuilder portfolioSummary = new StringBuilder();
        for (Trade trade : trades) {
            StringBuilder append = portfolioSummary.append(trade.isBuy() ? "Bought" : "Sold")
                    .append(" ")
                    .append(trade.getQuantity())
                    .append(" stocks of ")
                    .append(trade.getSymbol())
                    .append("to $")
                    .append(trade.getPriceAtExecution())
                    .append(" le ")
                    .append(trade.getTimestamp())
                    .append(". Reason : ")
                    .append(trade.getDecisionReason())
                    .append("\n");
        }

        // Call to AI
        String aiResponse = aiService.getMarketSummary(userQuestion, portfolioSummary.toString());

        return ResponseEntity.ok(Map.of("response", aiResponse));
    }
}
