package com.Ethica.demo.Service;

import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Repo.TradingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import yahoofinance.Stock;

@Service
public class portfolioService {

    @Autowired
    TradingRepository tradingRepository;

    public void buyStocks(String symbol,double priceAtExecution,double quantity,String decisionReason,boolean isBuy){

        Trade trade = new Trade();
        trade.setSymbol(symbol);
        trade.setQuantity(quantity);
        trade.setPriceAtExecution(priceAtExecution);
        trade.setDecisionReason(decisionReason);
        trade.setBuy(true);
        tradingRepository.save(trade);


    }

    public void sellStocks(String symbol,double priceAtExecution,double quantity,String decisionReason,boolean isBuy){
        Trade trade = new Trade();
        trade.setSymbol(symbol);
        trade.setQuantity(quantity);
        trade.setPriceAtExecution(priceAtExecution);
        trade.setDecisionReason(decisionReason);
        trade.setBuy(false);
    }


}
