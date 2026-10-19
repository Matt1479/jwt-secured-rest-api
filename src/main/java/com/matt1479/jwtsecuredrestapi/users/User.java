package com.matt1479.jwtsecuredrestapi.users;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank 
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank 
    @Column(nullable = false)
    private String passwordHash;

    @NotEmpty 
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles = new HashSet<>();

    // required by JPA; application code should use the parameterized constructor
    protected User() {}

    public User(String username, String passwordHash, Set<Role> roles) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("PasswordHash is required");
        }

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Every user must have at least one role");
        }

        this.username = username;
        this.passwordHash = passwordHash;
        this.roles.addAll(roles);
    }

    @Override 
    public String toString() {
        return String.format(
            "User=[id=%s, username='%s', roles=%s]",
            id, username, roles
        );
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("PasswordHash is required");
        }

        this.passwordHash = passwordHash;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    public void setRoles(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Every user must have at least one role");
        }

        this.roles.clear();
        this.roles.addAll(roles);
    }

}
