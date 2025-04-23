package com.Ethica.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="Users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;
    @Column( nullable = false, length = 50)
    private String lastName;
    @Column( nullable = false,length = 50)
    private String password;
    @Column(unique = true, nullable = false,length = 50)
    private String email;

    @Column( nullable = false,length = 300)
    private String description;

    @Column( nullable = false,length = 50)
    private String InvestorProfil;


    @Column(nullable = false)
    private int age;

    // Constructeurs
    public User() {
    }

    public User(String firstName, String lastName, String email, String description, String InvestorProfil, String password,int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
        this.InvestorProfil = InvestorProfil;
        this.age = age;
        this.password = password;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return lastName;
    }

    public void setName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvestorProfil() {
        return InvestorProfil;
    }

    public void setInvestorProfil(String InvestorProfil) {
        this.InvestorProfil = InvestorProfil;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}





