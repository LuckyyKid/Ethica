package com.Ethica.demo.Service;

import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

@Service
public class StockService {



    public Stock getStock(String ticker) throws IOException {
        return YahooFinance.get(ticker);
    }

}
