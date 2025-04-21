package com.Ethica.demo.Service;

import com.Ethica.demo.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public Boolean checkUser(String email,String password){
        boolean user = userRepository.FindByEmailPassword(email,password)!=null;
    if(user){
        return true;
    }
    return false;
}
}
