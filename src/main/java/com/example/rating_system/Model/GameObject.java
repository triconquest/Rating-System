package com.example.rating_system.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class GameObject {
    @Id @GeneratedValue private UUID id;
    private String title;
    @Lob private String text;
    @ManyToOne private User author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
