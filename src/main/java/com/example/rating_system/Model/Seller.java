package com.example.rating_system.Model;

import jakarta.persistence.*;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Seller {
    @Id @GeneratedValue private UUID id;
    @ManyToOne private User user;
    private String title;
    @Lob private String description;
    private String game;
    private boolean approved;
    private LocalDateTime createdAt;
}
