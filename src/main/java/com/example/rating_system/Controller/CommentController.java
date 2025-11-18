package com.example.rating_system.Controller;

import com.example.rating_system.DTO.CommentDto;
import com.example.rating_system.Model.Comment;
import com.example.rating_system.Services.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> addComment(@PathVariable UUID sellerId, @Valid @RequestBody CommentDto dto, HttpServletRequest request) {
        return commentService.addComment(sellerId, dto, request);
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
                                           HttpServletRequest request,
                                           @RequestParam(required = false) String anonymousToken)
    {
        return commentService.updateComment(commentId, dto, request, anonymousToken);
    }

    // get every comment linked to this seller
    @GetMapping
    public List<CommentDto> getApprovedComments(@PathVariable UUID sellerId) {
        return commentService.getApprovedComments(sellerId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable UUID commentId,
                                HttpServletRequest request,
                                @RequestParam(required = false) String token) {
        return commentService.deleteComment(commentId, request, token);
    }
}
