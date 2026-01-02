package com.Ethica.demo.controller;

import com.Ethica.demo.entity.ClientPortfolio;
import com.Ethica.demo.entity.User;
import com.Ethica.demo.repo.PortfolioRepository;
import com.Ethica.demo.repo.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AddUserControlleur {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    public AddUserControlleur(
            UserRepository userRepository,
            PortfolioRepository portfolioRepository
    ) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @GetMapping("/signUp")
    public String showSignUpPage() {
        return "signUp";
    }

    @PostMapping("/signUp")
    public String addUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String age,
            @RequestParam String investorProfile,
            @RequestParam String description,
            RedirectAttributes redirectAttributes
    ) {
        // Check if email already exists
        if (userRepository.findByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("error", "This email is already associated with an account. Please use a different email.");
            redirectAttributes.addFlashAttribute("firstName", firstName);
            redirectAttributes.addFlashAttribute("lastName", lastName);
            redirectAttributes.addFlashAttribute("age", age);
            redirectAttributes.addFlashAttribute("investorProfile", investorProfile);
            redirectAttributes.addFlashAttribute("description", description);
            return "redirect:/signUp";
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setAge(Integer.parseInt(age));
        user.setInvestorProfil(investorProfile);
        user.setDescription(description);

        userRepository.save(user);

        ClientPortfolio portfolio = new ClientPortfolio();
        portfolio.setBalance(0.0);
        portfolio.setPerformancePercentage(0.0);
        portfolio.setUser(user);

        portfolioRepository.save(portfolio);

        // Add success message
        redirectAttributes.addFlashAttribute("success", "Account created successfully! Please sign in.");
        return "redirect:/login";
    }
}