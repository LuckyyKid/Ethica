package com.Ethica.demo.Entity;

import java.math.BigDecimal;

public class StockDTO {
    private String symbol;
    private String name;
    private double price;
    private double percentChange;

    public StockDTO(String symbol, String name, double price, double percentChange) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.percentChange = percentChange;
    }

    // Getters
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getPercentChange() { return percentChange; }
}

