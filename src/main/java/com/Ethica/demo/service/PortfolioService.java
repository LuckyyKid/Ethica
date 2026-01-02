package com.Ethica.demo.service;

import com.Ethica.demo.entity.ClientPortfolio;
import com.Ethica.demo.entity.Trade;
import com.Ethica.demo.entity.User;
import com.Ethica.demo.repo.PortfolioRepository;
import com.Ethica.demo.repo.TradingRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final TradingRepository tradingRepository;

    public PortfolioService(
            PortfolioRepository portfolioRepository,
            TradingRepository tradingRepository
    ) {
        this.portfolioRepository = portfolioRepository;
        this.tradingRepository = tradingRepository;
    }

    /**
     * Récupère le portfolio d'un utilisateur
     */
    public ClientPortfolio getCurrentPortfolio(User sessionUser) {
        return portfolioRepository.findByUser(sessionUser)
                .orElseThrow(() -> new RuntimeException("No portfolio found for this user."));
    }

    /**
     * Récupère ou crée un portfolio pour un utilisateur
     */
    public ClientPortfolio getOrCreatePortfolio(User user) {
        return portfolioRepository.findByUser(user).orElseGet(() -> {
            ClientPortfolio newPortfolio = new ClientPortfolio();
            newPortfolio.setUser(user);
            newPortfolio.setBalance(0.0);
            newPortfolio.setPerformancePercentage(0.0);
            return portfolioRepository.save(newPortfolio);
        });
    }

    /**
     * Récupère les trades d'un portfolio triés par date
     */
    public List<Trade> getTradesByPortfolio(ClientPortfolio portfolio) {
        return tradingRepository.findByPortfolioOrderByTimestampAsc(portfolio);
    }

    /**
     * Calcule les statistiques du portfolio (P&L, positions, historique)
     */
    public PortfolioStats calculatePortfolioStats(ClientPortfolio portfolio) {
        List<Trade> trades = getTradesByPortfolio(portfolio);
        return calculatePortfolioStats(trades);
    }

    /**
     * Calcule les statistiques à partir d'une liste de trades
     */
    public PortfolioStats calculatePortfolioStats(List<Trade> trades) {
        // Track positions par symbole: {totalQty, totalCost, avgPrice}
        Map<String, double[]> positions = new HashMap<>();

        List<Map<String, Object>> chartPoints = new ArrayList<>();
        double totalRealizedPnL = 0.0;
        double totalInvested = 0.0;

        for (Trade trade : trades) {
            String symbol = trade.getSymbol();
            double qty = trade.getQuantity();
            double price = trade.getPriceAtExecution();
            double tradeValue = qty * price;

            // [0] = totalQty, [1] = totalCost, [2] = avgPrice
            double[] pos = positions.getOrDefault(symbol, new double[]{0, 0, 0});

            if (trade.isBuy()) {
                // ACHAT: Augmente la position, recalcule le prix moyen
                double newTotalCost = pos[1] + tradeValue;
                double newTotalQty = pos[0] + qty;
                double newAvgPrice = newTotalCost / newTotalQty;

                pos[0] = newTotalQty;
                pos[1] = newTotalCost;
                pos[2] = newAvgPrice;

                totalInvested += tradeValue;

            } else {
                // VENTE: Calcule le P&L réalisé
                if (pos[0] > 0) {
                    double costBasis = qty * pos[2];
                    double saleProceeds = tradeValue;
                    double pnl = saleProceeds - costBasis;

                    totalRealizedPnL += pnl;

                    pos[0] -= qty;
                    pos[1] -= costBasis;
                    totalInvested -= costBasis;

                    if (pos[0] <= 0) {
                        pos[0] = 0;
                        pos[1] = 0;
                        pos[2] = 0;
                    }
                }
            }

            positions.put(symbol, pos);

            // Point sur le graphique
            double portfolioValue = totalInvested + totalRealizedPnL;

            Map<String, Object> point = new HashMap<>();
            point.put(
                    "date",
                    trade.getTimestamp().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    )
            );
            point.put("balance", portfolioValue);
            chartPoints.add(point);
        }

        // Calcul du pourcentage de performance
        double performancePercentage = 0.0;
        if (totalInvested > 0) {
            performancePercentage = (totalRealizedPnL / totalInvested) * 100;
        }

        return new PortfolioStats(
                chartPoints,
                totalRealizedPnL,
                totalInvested,
                totalInvested + totalRealizedPnL,
                performancePercentage,
                positions
        );
    }

    /**
     * Classe interne pour encapsuler les statistiques du portfolio
     */
    public static class PortfolioStats {
        private final List<Map<String, Object>> chartPoints;
        private final double totalRealizedPnL;
        private final double totalInvested;
        private final double portfolioValue;
        private final double performancePercentage;
        private final Map<String, double[]> positions;

        public PortfolioStats(
                List<Map<String, Object>> chartPoints,
                double totalRealizedPnL,
                double totalInvested,
                double portfolioValue,
                double performancePercentage,
                Map<String, double[]> positions
        ) {
            this.chartPoints = chartPoints;
            this.totalRealizedPnL = totalRealizedPnL;
            this.totalInvested = totalInvested;
            this.portfolioValue = portfolioValue;
            this.performancePercentage = performancePercentage;
            this.positions = positions;
        }

        public List<Map<String, Object>> getChartPoints() {
            return chartPoints;
        }

        public double getTotalRealizedPnL() {
            return totalRealizedPnL;
        }

        public double getTotalInvested() {
            return totalInvested;
        }

        public double getPortfolioValue() {
            return portfolioValue;
        }

        public double getPerformancePercentage() {
            return performancePercentage;
        }

        public Map<String, double[]> getPositions() {
            return positions;
        }

        /**
         * Retourne la position pour un symbole donné
         * @return [totalQty, totalCost, avgPrice] ou null si pas de position
         */
        public double[] getPosition(String symbol) {
            return positions.get(symbol);
        }

        /**
         * Vérifie si le portfolio est en profit
         */
        public boolean isInProfit() {
            return totalRealizedPnL > 0;
        }
    }
}