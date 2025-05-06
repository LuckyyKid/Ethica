package com.Ethica.demo.Service;

import com.Ethica.demo.Repo.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyStocks {

    @Autowired
    StocksRepository stocksRepository;

}
