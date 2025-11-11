package com.example.rating_system.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class User {
    @Id @GeneratedValue private UUID id;
    private String firstName;
    private String lastName;
    @Column (unique = true) private String mail;
    private String passwordHash;
    private LocalDateTime createdAt;
    @Enumerated (EnumType.STRING) private Role role;
    private boolean enabled;


    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return mail; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String mail) {
        this.mail = mail;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
