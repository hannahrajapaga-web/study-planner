package com.example.studyplanner.controller;

import com.example.studyplanner.config.JwtTokenProvider;
import com.example.studyplanner.model.User;
import com.example.studyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173","https://hannahstudyplanner.vercel.app"}) // Allow requests from React frontend
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Invalid username or password"));

            // Check if the user password matches the BCrypt password in the DB
            if (passwordEncoder.matches(password, user.getPassword())) {
                // If success, generate the VIP JWT Wristband
                String token = tokenProvider.generateToken(username);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("userId", user.getId());
                response.put("username", user.getUsername());
                response.put("studyGoal", user.getStudyGoal());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}