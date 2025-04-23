package com.Ethica.demo.Repo;

import com.Ethica.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

        @Query("SELECT u FROM User u WHERE u.firstName = :firstName")
       public String VerifyUser(@Param("firstName") String firstName);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User FindByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email And u.password = :password  ")
    public User FindByEmailPassword(@Param("email")String email ,@Param("password")  String  password);




}
