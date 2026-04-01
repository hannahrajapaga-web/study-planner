package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import com.example.demo.model.LoginResponse;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository repo;

    // Signup method
    public User signup(User user){
        if (user.getName() != null) user.setName(user.getName().trim());
        if (user.getEmail() != null) user.setEmail(user.getEmail().trim().toLowerCase());
        if (user.getPassword() != null) user.setPassword(user.getPassword().trim());

        User savedUser = repo.save(user);
        System.out.println("Saved user: " + savedUser);
        return savedUser;
    }

    // Login method
    public LoginResponse login(String email, String password) {
        try {
            if (email == null || password == null) {
                return new LoginResponse("error", "Email or password missing", null);
            }

            email = email.trim().toLowerCase();
            password = password.trim();

            System.out.println("Login attempt: email='" + email + "', password='" + password + "'");

            User u = repo.findByEmail(email);

            if (u == null) {
                return new LoginResponse("error", "Email not found", null);
            }

            if (u.getPassword() == null) {
                return new LoginResponse("error", "User password not set", null);
            }

            if (!u.getPassword().equals(password)) {
                return new LoginResponse("error", "Incorrect password", null);
            }

            return new LoginResponse("success", "Login successful", u);
        } catch (Exception e) {
            // Catch any unexpected exception and prevent 500
            e.printStackTrace();
            return new LoginResponse("error", "Internal server error", null);
        }
    }
}