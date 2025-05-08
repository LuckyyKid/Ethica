package com.Ethica.demo.Service;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.Trade;
import com.Ethica.demo.Repo.PortfolioRepository;
import com.Ethica.demo.Repo.TradingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    @Autowired
    private TradingRepository tradingRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    public void updateBalanceAfterTrade(ClientPortfolio clientPortfolio, Trade trade) {
        double quantity = trade.getQuantity();
        double priceAtExecution = trade.getPriceAtExecution();
        double dollarAmount = quantity * priceAtExecution;
        double currentBalance = clientPortfolio.getBalance();

        if (trade.isBuy()) {
            clientPortfolio.setBalance(currentBalance + dollarAmount);
        } else {
            clientPortfolio.setBalance(currentBalance - dollarAmount);
        }

        portfolioRepository.save(clientPortfolio);
    }
    public void handleTradeSubmission(Trade trade) {
        ClientPortfolio portfolio = trade.getPortfolio();
        tradingRepository.save(trade);
        updateBalanceAfterTrade(portfolio, trade);
    }
}
