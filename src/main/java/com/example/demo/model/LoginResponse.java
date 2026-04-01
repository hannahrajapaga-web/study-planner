package com.example.demo.model;

public class LoginResponse {
    private String status; // "success" or "error"
    private String message;
    private User user; // null if login fails

    public LoginResponse() {}

    public LoginResponse(String status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}