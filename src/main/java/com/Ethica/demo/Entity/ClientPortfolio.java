package com.Ethica.demo.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "portfolio")
public class ClientPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double balance;
    private double performancePercentage;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<Trade> trades;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    // Getters/setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getPerformancePercentage() {
        return performancePercentage;
    }

    public void setPerformancePercentage(double performancePercentage) {
        this.performancePercentage = performancePercentage;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
