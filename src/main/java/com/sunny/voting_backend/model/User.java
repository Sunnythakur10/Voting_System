package com.sunny.voting_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // 1. Lowercase is better for Postgres
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean hasVoted;

    // Default Constructor
    public User() {
    }

    // 2. Parameterized Constructor
    // NOTICE: I removed 'hasVoted' from the brackets (). It is not needed.
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.hasVoted = false; // Logic: New user = has not voted.
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } // Use Long wrapper to match field

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isHasVoted() { return hasVoted; } // Standard boolean naming is 'is...'
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }
}