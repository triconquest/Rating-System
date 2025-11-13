package com.example.rating_system.Controller;

import com.example.rating_system.DTO.CommentDto;
import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.CommentStatus;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.CommentRepository;
import com.example.rating_system.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentController(CommentRepository commentRepository,
                             UserRepository userRepository)
    {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Comment addComment(@PathVariable UUID sellerId, @RequestBody CommentDto dto) {
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));

        Comment comment = new Comment();
        comment.setMessage(dto.getMessage());
        comment.setSeller(seller);

        if(dto.getAuthorId() != null)
        {
            User author = userRepository.findById(dto.getAuthorId()).orElseThrow(() -> new RuntimeException("Author not found"));
            comment.setAuthor(author);
            comment.setAnonymousToken(null);
        }
        else {
            String token = UUID.randomUUID().toString();
            comment.setAnonymousToken(token);
            comment.setAuthor(null);
        }

        comment.setStatus(CommentStatus.PENDING);
        return commentRepository.save(comment);
    }

    // admin gets pending comments
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Comment> getPendingComments() {
        return commentRepository.findAllByStatus(CommentStatus.PENDING);
    }

    // admin approves/declines a comment
    @PutMapping("/{commentId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> moderateComment(@PathVariable UUID commentId, @RequestParam CommentStatus status)
    {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setStatus(status);
        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }

    // get a specific comment
    @GetMapping("/{commentId}")
    public Comment getSpecificComment(@PathVariable UUID commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    // update a specific comment
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable UUID commentId,
                                           @RequestBody CommentDto dto,
                                           @RequestParam(required = false) UUID authorId,
                                           @RequestParam(required = false) String anonymousToken)
    {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found."));

        // registered user updates a comment
        if(comment.getAuthor() != null) {
            if(!comment.getAuthor().getId().equals(authorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own comments.");
            }
        }

        // anonymous user updates a comment
        else if(comment.getAnonymousToken() != null) {
            if(!comment.getAnonymousToken().equals(anonymousToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid anonymous token. You can only update your own comments");
            }
        }

        comment.setMessage(dto.getMessage());
        comment.setStatus(CommentStatus.PENDING); // updated comment needs to be re-approved

        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }

    // get every comment linked to this seller
    @GetMapping
    public List<Comment> getApprovedComments(@PathVariable UUID sellerId) {
        return commentRepository.findAllBySellerIdAndStatus(sellerId, CommentStatus.APPROVED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable UUID commentId,
                                @RequestParam(required = false) UUID authorId,
                                @RequestParam(required = false) String token) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        // registered user
        if(comment.getAuthor() != null)
        {
            if(!comment.getAuthor().getId().equals(authorId))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own comments.");
        }

        // anonymous user
        else if(comment.getAnonymousToken() != null)
        {
            if(!comment.getAnonymousToken().equals(token))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid anonymous token, you can only delete your own comments.");
        }

        commentRepository.delete(comment);
        return ResponseEntity.ok("Comment deleted successfully.");
    }
}
