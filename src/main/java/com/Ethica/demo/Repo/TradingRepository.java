package com.Ethica.demo.Repo;

import com.Ethica.demo.Entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingRepository extends JpaRepository<Trade,Long> {

}
