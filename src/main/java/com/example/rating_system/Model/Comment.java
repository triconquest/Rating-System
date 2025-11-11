package com.example.rating_system.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Comment {
    @Id @GeneratedValue UUID id;
    @Lob private String message;
    @ManyToOne private User author;
    @ManyToOne private Seller seller;
    private LocalDateTime createdAt;
    private boolean approved;
    private Integer ratingValue;
}
