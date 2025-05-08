package com.Ethica.demo.Repo;

import com.Ethica.demo.Entity.ClientPortfolio;
import com.Ethica.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<ClientPortfolio, Long> {
    Optional<ClientPortfolio> findByUser(User user);

}
