package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Repo.TradingRepository;
import com.Ethica.demo.Service.portfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class portfolioControlleur {

    @Autowired
    portfolioService portfolioService;
    @Autowired
    private TradingRepository tradingRepository;

    @GetMapping("/clientPortfolio")
    public String showPortfolio(Model model, HttpSession session) throws Exception {

        User currentUser = (User) session.getAttribute("userConnecte");

        if (currentUser == null) {
            return "redirect:/login"; // Redirection si non connecté
        }


        ClientPortfolio portfolio = portfolioService.getCurrentPortfolio(currentUser);


        List<Trade> trades = tradingRepository.findByPortfolioOrderByTimestampAsc(portfolio);


        List<Map<String, Object>> chartPoints = new ArrayList<>();
        double balance = 0;

        for (Trade trade : trades) {
            double delta = trade.getQuantity() * trade.getPriceAtExecution();
            balance += trade.isBuy() ? delta : -delta;

            Map<String, Object> point = new HashMap<>();
            point.put("date", trade.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            point.put("balance", balance);
            chartPoints.add(point);
        }


        ObjectMapper mapper = new ObjectMapper();

        String balancesJson = mapper.writeValueAsString(chartPoints);
        model.addAttribute("balancesJson", balancesJson);
        model.addAttribute("trades", trades);
        model.addAttribute("portfolio", portfolio);



        return "clientPortfolio";
    }

}
