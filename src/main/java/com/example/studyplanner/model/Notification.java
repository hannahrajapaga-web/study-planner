package com.example.studyplanner.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Boolean isRead = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Notification() {}

    // From:
    // public Notification(Long id, String message, LocalDateTime createdAt, boolean isRead, User user)
    
    // To (Capital B):
    public Notification(Long id, String message, LocalDateTime createdAt, Boolean isRead, User user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean isRead() { return isRead; }
    public void setRead(Boolean isRead) { this.isRead = isRead; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}