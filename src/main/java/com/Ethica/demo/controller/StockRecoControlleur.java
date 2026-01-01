package com.Ethica.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StockRecoControlleur {

    @GetMapping("/stockReco")
    public String showRecoPage(){
        return "stockReco";

    }
}
