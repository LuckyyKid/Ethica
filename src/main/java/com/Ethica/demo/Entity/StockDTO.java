package com.Ethica.demo.Entity;

import java.math.BigDecimal;

public class StockDTO {
    private String symbol;
    private BigDecimal price;

    public StockDTO(String symbol, BigDecimal price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public BigDecimal getPrice() { return price; }
}
