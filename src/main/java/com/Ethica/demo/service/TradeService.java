package com.Ethica.demo.service;

import com.Ethica.demo.entity.ClientPortfolio;
import com.Ethica.demo.entity.Trade;
import com.Ethica.demo.repo.PortfolioRepository;
import com.Ethica.demo.repo.TradingRepository;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    private final TradingRepository tradingRepository;
    private final PortfolioRepository portfolioRepository;

    public TradeService(TradingRepository tradingRepository,
                        PortfolioRepository portfolioRepository) {
        this.tradingRepository = tradingRepository;
        this.portfolioRepository = portfolioRepository;
    }

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
