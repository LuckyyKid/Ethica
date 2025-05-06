package com.Ethica.demo.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private double priceAtExecution;
    private double quantity;
    private String decisionReason;


    private boolean isBuy;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private ClientPortfolio portfolio;

    public Trade() {}

    public Trade(String symbol, double priceAtExecution, double quantity, String decisionReason, boolean isBuy, LocalDateTime timestamp, ClientPortfolio portfolio) {
        this.symbol = symbol;
        this.priceAtExecution = priceAtExecution;
        this.quantity = quantity;
        this.decisionReason = decisionReason;
        this.isBuy = isBuy;
        this.timestamp = timestamp;
        this.portfolio = portfolio;
    }

    // Getters & Setters
    public Long getId() { return id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getPriceAtExecution() { return priceAtExecution; }
    public void setPriceAtExecution(double priceAtExecution) { this.priceAtExecution = priceAtExecution; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getDecisionReason() { return decisionReason; }
    public void setDecisionReason(String decisionReason) { this.decisionReason = decisionReason; }

    public boolean isBuy() { return isBuy; }
    public void setBuy(boolean buy) { isBuy = buy; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public ClientPortfolio getPortfolio() { return portfolio; }
    public void setPortfolio(ClientPortfolio portfolio) { this.portfolio = portfolio; }
}
