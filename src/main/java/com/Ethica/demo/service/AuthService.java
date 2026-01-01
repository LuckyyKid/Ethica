package com.Ethica.demo.service;

import com.Ethica.demo.entity.User;
import com.Ethica.demo.repo.UserRepository;
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
