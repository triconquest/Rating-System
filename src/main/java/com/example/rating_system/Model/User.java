package com.example.rating_system.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue private UUID id;
    private String firstName;
    private String lastName;
    @Column (unique = true) private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
    @Enumerated (EnumType.STRING) private Role role;
    private boolean approved = false;
    private boolean emailConfirmed = false;


    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public boolean isEmailConfirmed() { return emailConfirmed; }
    public boolean isApproved() { return approved; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public void setApproved(boolean approved) { this.approved = approved; }
    public void setEmailConfirmed(boolean emailConfirmed) { this.emailConfirmed = emailConfirmed; }
    public void setRole(Role role) { this.role = role; }
}
