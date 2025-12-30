package com.Ethica.demo.Controller;

import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Service.ScoringService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scoring")
@CrossOrigin(origins = "http://localhost:5173")
public class ScoringController {

    private final ScoringService scoringService;

    public ScoringController(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @PostMapping
    public List<Map<String, Object>> getRecommendations(HttpSession session) {

        User currentUser = (User) session.getAttribute("userConnecte");

        if (currentUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User is not connected"
            );
        }

        return scoringService.getStockRecommendations(
                currentUser.getDescription(),
                currentUser.getInvestorProfil(),
                currentUser.getAge()
        );
    }
}
