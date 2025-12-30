package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.StockDTO;
import com.Ethica.demo.Service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class SearchStockControlleur {

    private static final String DASHBOARD_VIEW = "dashboard";

    private final StockService stockService;

    public SearchStockControlleur(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/dashboard")
    public String searchStock(
            @RequestParam(required = false) String ticker,
            Model model
    ) {

        if (ticker != null && !ticker.isBlank()) {
            try {
                StockDTO stock = stockService.getStock(ticker.toUpperCase());

                if (stock == null || stock.getPrice() == 0.0) {
                    model.addAttribute("error", "No result found for: " + ticker);
                } else {
                    model.addAttribute("stock", stock);
                }

            } catch (IOException e) {
                model.addAttribute(
                        "error",
                        "An error occurred while retrieving data for: " + ticker
                );
            }
        }

        return DASHBOARD_VIEW;
    }
}
