package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UsersRepository;

public interface UserService {

     User login(String accountName, String password);

     boolean logout(String accountName);

     User register(String accountName,String firstName,String lastName, String email ,String password);

     void verifyUser(String accountName, String verificationCode);

     void addUser(User user);

     void addUserThenAutoActive(User user);

     boolean activateUser(String actiation_code   );

     boolean updatePassword(String accountName, String password);

     boolean updateEmail(String email, String newEmail);

}
