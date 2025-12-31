package com.Ethica.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String  showPageIndex(){
        return "index";
    }

    @GetMapping("/index")
    public String  showPageHome(){
        return "index";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    // Tu peux ajouter d'autres pages statiques ici
    @GetMapping("/privacy")
    public String privacyPage() {
        return "privacy";
    }

    @GetMapping("/terms")
    public String termsPage() {
        return "terms";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

}
