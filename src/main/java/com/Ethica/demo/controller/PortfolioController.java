package com.Ethica.demo.controller;

import com.Ethica.demo.entity.ClientPortfolio;
import com.Ethica.demo.entity.Trade;
import com.Ethica.demo.entity.User;
import com.Ethica.demo.service.PortfolioService;
import com.Ethica.demo.service.PortfolioService.PortfolioStats;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final ObjectMapper objectMapper;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/clientPortfolio")
    public String showPortfolio(Model model, HttpSession session)
            throws JsonProcessingException {

        // Vérifier l'authentification
        User currentUser = (User) session.getAttribute("userConnecte");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Récupérer le portfolio
        ClientPortfolio portfolio = portfolioService.getCurrentPortfolio(currentUser);

        // Récupérer les trades
        List<Trade> trades = portfolioService.getTradesByPortfolio(portfolio);

        // Calculer les statistiques via le service
        PortfolioStats stats = portfolioService.calculatePortfolioStats(trades);

        // Convertir les points du graphique en JSON
        String balancesJson = objectMapper.writeValueAsString(stats.getChartPoints());

        // Passer les données à la vue
        model.addAttribute("balancesJson", balancesJson);
        model.addAttribute("trades", trades);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("totalPnL", stats.getTotalRealizedPnL());
        model.addAttribute("totalInvested", stats.getTotalInvested());
        model.addAttribute("portfolioValue", stats.getPortfolioValue());
        model.addAttribute("performancePercentage", stats.getPerformancePercentage());
        model.addAttribute("isInProfit", stats.isInProfit());

        return "clientPortfolio";
    }
}