package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Service.ScoringService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scoring")
@CrossOrigin(origins = "http://localhost:5173")
public class ScoringController {

    @Autowired
    private ScoringService scoringService;

    @PostMapping
    public List<Map<String, Object>> getRecommendations(HttpSession session) {
        User currentUser = (User) session.getAttribute("userConnecte");

        if (currentUser == null) {
            throw new RuntimeException("User is not connected.");
        }

        String description = currentUser.getDescription();
        String profile = currentUser.getInvestorProfil();
        int age = currentUser.getAge();

        return scoringService.getStockRecommendations(description, profile, age);
    }
}
