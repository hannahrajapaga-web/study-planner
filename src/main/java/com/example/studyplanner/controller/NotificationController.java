package com.example.studyplanner.controller;

import com.example.studyplanner.model.Notification;
import com.example.studyplanner.model.User;
import com.example.studyplanner.service.NotificationService;
import com.example.studyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        return ResponseEntity.ok(notificationService.getNotificationsByUser(user.getId()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}