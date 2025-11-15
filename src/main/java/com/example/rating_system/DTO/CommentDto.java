package com.example.rating_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CommentDto {

    @NotBlank(message = "Message can't be blank")
    private String message;

    private UUID authorId;

    @NotNull(message = "Rating can't be null")
    private Integer rating;

    public String getMessage() { return message; }
    public UUID getAuthorId() { return authorId; }
    public Integer getRating() { return rating; }

    public void setMessage(String message) { this.message = message; }
    public void setAuthorId(UUID authorId) { this.authorId = authorId; }
    public void setRating(Integer rating) { this.rating = rating; }
}
