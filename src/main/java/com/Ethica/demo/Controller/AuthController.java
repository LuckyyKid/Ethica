package com.Ethica.demo.Controller;


import com.Ethica.demo.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLoginPage(){
        System.out.println("LOGIN CONTROLLER OK");
        return "login";

    }

    @PostMapping("/login")
    public String authenticate(@RequestParam String email, @RequestParam String password) {
        if (authService.checkUser(email, password)== true) {
            return "dashboard";
        }
        return "login";
    }

    }




