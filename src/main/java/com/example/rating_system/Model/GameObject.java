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
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public User getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setAuthor(User author) { this.author = author; }
}
