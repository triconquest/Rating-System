package com.example.rating_system.DTO;

import java.util.UUID;

public class CommentDto {
    private String message;
    private UUID authorId;
    private Integer rating;

    public String getMessage() { return message; }
    public UUID getAuthorId() { return authorId; }
    public Integer getRating() { return rating; }

    public void setMessage(String message) { this.message = message; }
    public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public void setRating(Integer rating) { this.rating = rating; }
}
