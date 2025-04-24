package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.StockDTO;
import com.Ethica.demo.Service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yahoofinance.Stock;

import java.io.IOException;
import java.math.BigDecimal;

@Controller
public class SearchStockControlleur {

    @Autowired
    private StockService stockService;

    @GetMapping("/dashboard")
    public String searchStock(@RequestParam(required = false) String ticker, Model model) {
        if (ticker == null || ticker.isBlank()) {
            return "dashboard";
        }

        try {
            Stock stock = stockService.getStock(ticker.toUpperCase());
            if (stock == null || stock.getQuote().getPrice() == null) {
                model.addAttribute("error", "No result found for: " + ticker);
                return "dashboard";
            }

            model.addAttribute("stock", new StockDTO(stock.getSymbol(), stock.getQuote().getPrice()));
        } catch (IOException e) {
            model.addAttribute("error", "An error occurred while retrieving data for: " + ticker);
        }

        return "dashboard";
    }
}

