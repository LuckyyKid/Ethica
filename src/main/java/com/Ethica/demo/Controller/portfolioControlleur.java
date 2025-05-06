package com.Ethica.demo.Controller;

import com.Ethica.demo.Service.portfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class portfolioControlleur {

    @Autowired
    portfolioService portfolioService;

    @GetMapping("/clientPortfolio")
    public String showPortfolio() {
        return "clientPortfolio";
    }
}
