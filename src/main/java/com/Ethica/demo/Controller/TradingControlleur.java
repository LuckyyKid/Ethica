package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Repo.TradingRepository;
import com.Ethica.demo.Service.StockService;
import com.Ethica.demo.Service.TradeService;
import com.Ethica.demo.Service.portfolioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class TradingControlleur {



    @Autowired
    TradeService tradeService;

    @Autowired
    private TradingRepository tradingRepository;

    @Autowired
    private portfolioService portfolioServices;

    @GetMapping("/trading")
    public String tradingPage(){
        return "trading";
    }
    @PostMapping("/trading")

    public String Trade(@RequestParam String symbol, @RequestParam String priceAtExecution , @RequestParam String quantity, @RequestParam String type, @RequestParam String decisionReason, HttpSession session) {
        User currentUser = (User) session.getAttribute("userConnecte");

        if (currentUser == null) {
            return "redirect:/login";
        }
        Trade trade = new Trade();
        trade.setSymbol(symbol);
        trade.setPriceAtExecution(Double.parseDouble(priceAtExecution));
        trade.setQuantity(Double.parseDouble(quantity));
        trade.setDecisionReason(decisionReason);
        trade.setBuy(type.equalsIgnoreCase("buy"));
        trade.setTimestamp(LocalDateTime.now());
        ClientPortfolio clientPortfolio = portfolioServices.getCurrentPortfolio(currentUser);
        trade.setPortfolio(clientPortfolio);
        tradeService.handleTradeSubmission(trade);
        return "redirect:/clientPortfolio";
    }


}
