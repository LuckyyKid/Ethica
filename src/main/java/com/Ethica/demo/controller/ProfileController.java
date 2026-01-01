package com.Ethica.demo.controller;

import com.Ethica.demo.entity.User;
import com.Ethica.demo.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("userConnecte");
        if (user == null) {
            return "redirect:/login";
        }
        return "Profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("age") int age,
            @RequestParam("investorProfil") String investorProfil,
            @RequestParam("description") String description,
            HttpSession session,
            RedirectAttributes redirectAttributes) {


        User user = (User) session.getAttribute("userConnecte");
        if (user == null) {
            return "redirect:/login";
        }

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());
            if (optionalUser.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "User not found.");
                return "redirect:/profile";
            }

            User dbUser = optionalUser.get();


            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(dbUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "This email is already in use.");
                return "redirect:/profile";
            }

            //user info
            dbUser.setFirstName(firstName);
            dbUser.setName(lastName);  // setName() pour lastName
            dbUser.setEmail(email);
            dbUser.setAge(age);
            dbUser.setInvestorProfil(investorProfil);  // InvestorProfil (pas Profile)
            dbUser.setDescription(description);

            // Sauving  user info
            userRepository.save(dbUser);

            // updating user info
            session.setAttribute("userConnecte", dbUser);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return "redirect:/profile";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating your profile.");
            return "redirect:/profile";
        }
    }
}