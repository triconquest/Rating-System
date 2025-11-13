package com.example.rating_system.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class GameObject {
    @Id @GeneratedValue private UUID id;
    private String title;
    @Lob private String text;
    @ManyToOne private User seller;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public User getSeller() { return seller; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setSeller(User seller) { this.seller = seller; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
