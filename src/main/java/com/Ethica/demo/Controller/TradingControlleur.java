package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Repo.TradingRepository;
import com.Ethica.demo.Service.StockService;
import com.Ethica.demo.Service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TradingControlleur {



    @Autowired
    TradeService tradeService;
    @Autowired
    private TradingRepository tradingRepository;

    @GetMapping("/trading")
    public String tradingPage(){
        return "trading";
    }
    @PostMapping("/trading")

    public String Trade(@RequestParam String symbol, @RequestParam String priceAtExecution , @RequestParam String quantity, @RequestParam String type, @RequestParam String decisionReason) {
        Trade trade = new Trade();
        trade.setSymbol(symbol);
        trade.setPriceAtExecution(Double.parseDouble(priceAtExecution));
        trade.setQuantity(Double.parseDouble(quantity));
        trade.isBuy();
        trade.setDecisionReason(decisionReason);
        tradingRepository.save(trade);
        return "clientPortfolio";
    }


}
