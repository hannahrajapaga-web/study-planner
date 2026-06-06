package com.example.studyplanner.controller;

import com.example.studyplanner.model.User;
import com.example.studyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile/goal")
    public ResponseEntity<User> updateGoal(@RequestBody Map<String, String> request, Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        String goal = request.get("studyGoal");
        return ResponseEntity.ok(userService.updateStudyGoal(user.getId(), goal));
    }
}