package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.User;
import com.example.demo.model.LoginResponse;
import com.example.demo.service.UserService;
import com.example.demo.model.LoginRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody User user){
        User savedUser = service.signup(user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Signup successful");
        response.put("user", savedUser);

        return response;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return service.login(request.getEmail(), request.getPassword());
    }
}