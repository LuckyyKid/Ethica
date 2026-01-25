package com.ethica.ethica_backend.Repo;

import com.ethica.ethica_backend.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.firstName = :firstName")
    public String VerifyUser(@Param("firstName") String firstName);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User FindByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email And u.password = :password  ")
    public User FindByEmailPassword(@Param("email")String email ,@Param("password")  String  password);

    Optional<User> findByEmail(String email);

}
