package com.Ethica.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "portfolio")
public class clientPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;           // ex : AAPL
    private double purchasePrice;    // prix d'achat
    private double quantity;         // combien de parts
    private String decisionReason;   // raison de l'achat ou vente

    // Constructeur vide obligatoire pour JPA
    public clientPortfolio() {}

    public clientPortfolio(String symbol, double purchasePrice, double quantity, String decisionReason) {
        this.symbol = symbol;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.decisionReason = decisionReason;
    }

    // Getters et setters
    public Long getId() { return id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getDecisionReason() { return decisionReason; }
    public void setDecisionReason(String decisionReason) { this.decisionReason = decisionReason; }
}
