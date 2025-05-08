package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Repo.PortfolioRepository;
import com.Ethica.demo.Repo.UserRepository;
import com.Ethica.demo.Service.portfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddUserControlleur {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private portfolioService  portfolioService;

    @Autowired
    PortfolioRepository portfolioRepository;


    @GetMapping("/signUp")
    public String ShowSignUpPage(){
        return "signUp";
    }

    @PostMapping("/signUP")
    public String addUser(@RequestParam String firstName, @RequestParam String lastName , @RequestParam String email, @RequestParam String password, @RequestParam String age, @RequestParam String InvestorProfil, @RequestParam String description){
        User user = new User();
        user.setFirstName(firstName);
        user.setName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setAge(Integer.parseInt(age));
        user.setInvestorProfil(InvestorProfil);
        user.setDescription(description);
        userRepository.save(user);

        ClientPortfolio portfolio  = new ClientPortfolio();
        portfolio.setBalance(0.0);
        portfolio.setPerformancePercentage(0.0);
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);



        return "login";
    }

}
