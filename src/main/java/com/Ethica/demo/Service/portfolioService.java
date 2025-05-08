package com.Ethica.demo.Service;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Repo.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class portfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public ClientPortfolio getCurrentPortfolio(User sessionUser) {
        return portfolioRepository.findByUser(sessionUser)
                .orElseThrow(() -> new RuntimeException("Aucun portfolio trouvé pour cet utilisateur."));
}
    }
