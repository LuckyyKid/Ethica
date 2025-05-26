package com.ethica.ethica_backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.UUID;

@Entity
public class financialAdviser {
    @Id
    private Long id;

    @Column(length = 100)
    private String firstName;
    @Column(length = 100)
    private String lastName;
    @Column(length = 100)
    private String firmName;
    @Column(length = 100)
    private String phoneNumber;
    @Column(unique = true, length = 50)
    private String email;
    @Column(length = 100)
    private String password;
    @Column(length = 100)
    private String description;

    private Date inscriptionDate;






    public financialAdviser(Long id, String firstName, String lastName, String email, String password,String firmName, String phoneNumber, String description, Date inscriptionDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.firmName= firmName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.inscriptionDate = inscriptionDate;
    }

    public financialAdviser() {

    }



    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email= email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirmName() {
        return firmName;
    }
    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getInscriptionDate() {
        return inscriptionDate;
    }
    public void setInscriptionDate(Date inscriptionDate) {
        this.inscriptionDate = inscriptionDate;
    }

}
