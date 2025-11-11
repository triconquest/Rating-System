package com.example.rating_system.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = true)
    private User author;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public UUID getId() { return id; }
    public String getMessage() { return message; }
    public User getAuthor() { return author; }
    //public Seller getSeller() { return seller; }
    public User getSeller() { return seller; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public CommentStatus getStatus() { return status; }

    public void setMessage(String message) { this.message = message; }
    public void setAuthor(User author) { this.author = author; }
    public void setSeller(User seller) { this.seller = seller; }
    public void setStatus(CommentStatus status) { this.status = status; }

    //private boolean approved;

}
