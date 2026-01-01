package com.Ethica.demo.controller;

import com.Ethica.demo.entity.StockDTO;
import com.Ethica.demo.service.StockService;
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

        if (ticker == null || ticker.isBlank()) {
            return DASHBOARD_VIEW;
        }

        try {
            StockDTO stock = stockService.getStock(ticker.toUpperCase().trim());

            // Cas API OK mais ticker invalide
            if (stock == null || stock.getSymbol() == null) {
                model.addAttribute(
                        "error",
                        "The ticker you entered is not valid. Please try again."
                );
            } else {
                model.addAttribute("stock", stock);
            }

        } catch (Exception e) {
            // ⚠ Catch LARGE volontairement pour éviter TOUT 500
            model.addAttribute(
                    "error",
                    "The ticker you entered is not valid. Please try again."
            );
        }

        return DASHBOARD_VIEW;
    }
}
