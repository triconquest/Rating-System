package com.example.rating_system.Controller;

import com.example.rating_system.DTO.CommentDto;
import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.CommentStatus;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.CommentRepository;
import com.example.rating_system.Repository.UserRepository;
import com.example.rating_system.Services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{sellerId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService)
    {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment addComment(@PathVariable UUID sellerId, @Valid @RequestBody CommentDto dto) {
        return commentService.addComment(sellerId, dto);
    }

    // admin gets pending comments
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Comment> getPendingComments() {
        return commentService.getPendingComments();
    }

    // admin approves/declines a comment
    @PutMapping("/{commentId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> moderateComment(@PathVariable UUID commentId, @RequestParam CommentStatus status)
    {
        return commentService.moderateComment(commentId, status);
    }

    // get a specific comment
    @GetMapping("/{commentId}")
    public Comment getSpecificComment(@PathVariable UUID commentId) {
        return commentService.getSpecificComment(commentId);
    }

    // update a specific comment
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable UUID commentId,
                                           @Valid @RequestBody CommentDto dto,
                                           @RequestParam(required = false) UUID authorId,
                                           @RequestParam(required = false) String anonymousToken)
    {
        return commentService.updateComment(commentId, dto, authorId, anonymousToken);
    }

    // get every comment linked to this seller
    @GetMapping
    public List<Comment> getApprovedComments(@PathVariable UUID sellerId) {
        return commentService.getApprovedComments(sellerId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable UUID commentId,
                                @RequestParam(required = false) UUID authorId,
                                @RequestParam(required = false) String token) {
        return commentService.deleteComment(commentId, authorId, token);
    }
}
