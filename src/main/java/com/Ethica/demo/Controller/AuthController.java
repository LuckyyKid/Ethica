package com.Ethica.demo.Controller;
import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        logger.debug("Login page accessed");
        return "login";
    }


    @PostMapping("/login")
    public String authenticate(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            HttpServletResponse response,
             Model model
    ) {

        User user = authService.getUserByEmailPassword(email, password);

        if (user != null) {
            session.setAttribute("userConnecte", user);

            Cookie emailCookie = new Cookie("utilisateurEmail", user.getEmail());
            emailCookie.setMaxAge(60 * 60 * 24);
            emailCookie.setPath("/");
            response.addCookie(emailCookie);

            Cookie nameCookie = new Cookie("utilisateurNom", user.getFirstName());
            nameCookie.setMaxAge(60 * 60 * 24);
            nameCookie.setPath("/");
            response.addCookie(nameCookie);

            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid password or unknown user");
        return "login";
    }
}
