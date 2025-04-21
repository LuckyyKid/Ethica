package com.Ethica.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddUserControlleur {




    @GetMapping("/signUp")
    public String ShowSignUpPage(){
        return "signUp";
    }

    @PostMapping("/signUP")
    public String addUser(@RequestParam String firstName, @RequestParam String lastName ,@RequestParam String email, @RequestParam String password, @RequestParam String passwordConfirm, @RequestParam String age, @RequestParam String investorProfil){

        return "login";
    }
}
