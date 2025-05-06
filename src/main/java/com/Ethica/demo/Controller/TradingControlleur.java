package com.Ethica.demo.Controller;

import com.Ethica.demo.Service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TradingControlleur {



    @Autowired
    StockService stockService;

    @GetMapping("/trading")
    public String tradingPage(){
        return "trading";
    }
}
