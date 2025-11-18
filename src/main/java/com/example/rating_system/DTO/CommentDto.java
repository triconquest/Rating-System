package com.example.rating_system.DTO;

import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.CommentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentDto {

    private UUID id;

    @NotBlank(message = "Message can't be blank")
    private String message;

    @NotNull(message = "Rating can't be null")
    private Integer rating;

    private LocalDateTime createdAt;
    private CommentAuthorDto author;
    private CommentStatus status;
    private String anonymousToken;

    public static CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setRating(comment.getRating());
        dto.setStatus(comment.getStatus());
        dto.setCreatedAt(comment.getCreatedAt());

        if(comment.getAuthor() != null) {
            CommentAuthorDto authorDto = new CommentAuthorDto();
            authorDto.setId(comment.getAuthor().getId());
            authorDto.setFirstName(comment.getAuthor().getFirstName());
            authorDto.setLastName(comment.getAuthor().getLastName());
            dto.setAuthor(authorDto);
        }
        else {
            dto.setAuthor(null);
        }

        dto.setAnonymousToken(comment.getAnonymousToken());

        return dto;
    }

    public UUID getId() { return id; }
    public CommentAuthorDto getAuthor() { return author; }
    public String getMessage() { return message; }
    public Integer getRating() { return rating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public CommentStatus getStatus() { return status; }

    public void setId(UUID id) { this.id = id; }
    public void setStatus(CommentStatus status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setAuthor(CommentAuthorDto author) { this.author = author; }
    public void setAnonymousToken(String anonymousToken) { this.anonymousToken = anonymousToken; }
}
