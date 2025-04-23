package com.Ethica.demo.Controller;


import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    public String authenticate(@RequestParam String email, @RequestParam String password, HttpSession session, HttpServletResponse response) {

        // authService.checkUser doit retourner un objet User
        User user = authService.getUserByEmailPassword(email, password);

        if (user != null) {
            // Session (en mémoire du serveur)
            session.setAttribute("userConnecte", user);

            // Cookie (en mémoire dans le navigateur)
            Cookie emailCookie = new Cookie("utilisateurEmail", user.getEmail());
            emailCookie.setMaxAge(60 * 60 * 24); // 1 jour
            emailCookie.setPath("/");
            response.addCookie(emailCookie);

            Cookie nomCookie = new Cookie("utilisateurNom", user.getFirstName());
            nomCookie.setMaxAge(60 * 60 * 24); // 1 jour
            nomCookie.setPath("/");
            response.addCookie(nomCookie);

            return "dashboard";
        }
        return "login";
    }

    }




