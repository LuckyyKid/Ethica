package com.Ethica.demo.Service;


import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Repo.TradingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    @Autowired
    private TradingRepository  tradingRepository;




    public double userBalance( ClientPortfolio clientPortfolio , Trade trade){
        double quantity = trade.getQuantity();
        double priceAtExecution = trade.getPriceAtExecution();
        double DollarAmount = quantity*priceAtExecution;
       double clientBalance = clientPortfolio.getBalance();
       if(trade.isBuy()){
           clientPortfolio.setBalance(clientBalance+DollarAmount);

       }else{
           clientPortfolio.setBalance(clientBalance-DollarAmount);

       }
        return clientBalance;
    }
}
