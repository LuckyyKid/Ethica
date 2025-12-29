package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Repo.TradingRepository;
import com.Ethica.demo.Service.PortfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final TradingRepository tradingRepository;

    public PortfolioController(
            PortfolioService portfolioService,
            TradingRepository tradingRepository
    ) {
        this.portfolioService = portfolioService;
        this.tradingRepository = tradingRepository;
    }

    @GetMapping("/clientPortfolio")
    public String showPortfolio(Model model, HttpSession session) throws Exception {

        User currentUser = (User) session.getAttribute("userConnecte");

        if (currentUser == null) {
            return "redirect:/login";
        }

        ClientPortfolio portfolio = portfolioService.getCurrentPortfolio(currentUser);

        List<Trade> trades =
                tradingRepository.findByPortfolioOrderByTimestampAsc(portfolio);

        List<Map<String, Object>> chartPoints = new ArrayList<>();
        double balance = 0;

        for (Trade trade : trades) {
            double delta = trade.getQuantity() * trade.getPriceAtExecution();
            balance += trade.isBuy() ? delta : -delta;

            Map<String, Object> point = new HashMap<>();
            point.put(
                    "date",
                    trade.getTimestamp()
                            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            );
            point.put("balance", balance);
            chartPoints.add(point);
        }

        String balancesJson = new ObjectMapper().writeValueAsString(chartPoints);

        model.addAttribute("balancesJson", balancesJson);
        model.addAttribute("trades", trades);
        model.addAttribute("portfolio", portfolio);

        return "clientPortfolio";
    }
}
