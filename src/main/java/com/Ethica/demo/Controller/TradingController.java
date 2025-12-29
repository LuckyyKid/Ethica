package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Service.PortfolioService;
import com.Ethica.demo.Service.TradeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class TradingController {

    private final TradeService tradeService;
    private final PortfolioService portfolioService;

    public TradingController(
            TradeService tradeService,
            PortfolioService portfolioService
    ) {
        this.tradeService = tradeService;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/trading")
    public String tradingPage() {
        return "trading";
    }

    @PostMapping("/trading")
    public String trade(
            @RequestParam String symbol,
            @RequestParam String priceAtExecution,
            @RequestParam String quantity,
            @RequestParam String type,
            @RequestParam String decisionReason,
            HttpSession session
    ) {
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

        ClientPortfolio clientPortfolio =
                portfolioService.getCurrentPortfolio(currentUser);

        trade.setPortfolio(clientPortfolio);

        tradeService.handleTradeSubmission(trade);

        return "redirect:/clientPortfolio";
    }
}
