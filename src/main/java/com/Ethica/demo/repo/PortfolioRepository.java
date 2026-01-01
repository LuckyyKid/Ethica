package com.Ethica.demo.repo;

import com.Ethica.demo.entity.ClientPortfolio;
import com.Ethica.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<ClientPortfolio, Long> {
    Optional<ClientPortfolio> findByUser(User user);

}
