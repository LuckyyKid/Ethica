package com.Ethica.demo.Service;

import com.Ethica.demo.Entity.User;
import com.Ethica.demo.Repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmailPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .orElse(null);
    }
}
