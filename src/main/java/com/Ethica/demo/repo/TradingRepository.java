package com.Ethica.demo.repo;

import com.Ethica.demo.entity.ClientPortfolio;
import com.Ethica.demo.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingRepository extends JpaRepository<Trade,Long> {

    List<Trade> findByPortfolioOrderByTimestampAsc(ClientPortfolio portfolio);

}
