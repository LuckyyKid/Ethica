package Controller;


import Entity.User;
import Repo.UserRepository;
import Service.AuthService;
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
        return "login";
    }

    @PostMapping("/login")
    public String Authentication(@RequestParam String email,@RequestParam String Password ){
    if(authService.checkUser(email,Password) ==true)  {
        return "dashboard";
    }
    return "login";
    }

    }




