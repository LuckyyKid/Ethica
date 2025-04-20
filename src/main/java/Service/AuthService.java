package Service;

import Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public Boolean checkUser(String email,String password){
    if(userRepository.FindByEmailPassword(email,password)!=null){
        return true;
    }
    return false;
}
}
