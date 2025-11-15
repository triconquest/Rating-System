package com.example.rating_system.Services;

import com.example.rating_system.DTO.CommentDto;
import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.CommentStatus;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.CommentRepository;
import com.example.rating_system.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository)
    {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public Comment addComment(UUID sellerId, CommentDto dto) {
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));

        if(!seller.isApproved() || seller.getRole() != Role.ROLE_SELLER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot comment on a pending seller");
        }

        if(dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        Comment comment = new Comment();
        comment.setMessage(dto.getMessage());
        comment.setRating(dto.getRating());
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

    public List<Comment> getPendingComments() {
        return commentRepository.findAllByStatus(CommentStatus.PENDING);
    }

    // admin approves/declines a certain comment
    public ResponseEntity<?> moderateComment(UUID commentId, CommentStatus status)
    {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setStatus(status);
        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }

    public Comment getSpecificComment(UUID commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    // update specific comment
    public ResponseEntity<?> updateComment(UUID commentId,
                                           CommentDto dto,
                                           UUID authorId,
                                           String token)
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
            if(!comment.getAnonymousToken().equals(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid anonymous token. You can only update your own comments");
            }
        }

        comment.setMessage(dto.getMessage());
        comment.setRating(dto.getRating());
        comment.setStatus(CommentStatus.PENDING); // updated comment needs to be re-approved

        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }

    // get every comment linked to this user
    public List<Comment> getApprovedComments(UUID sellerId) {
        return commentRepository.findAllBySellerIdAndStatus(sellerId, CommentStatus.APPROVED);
    }

    public ResponseEntity<String> deleteComment(UUID commentId,
                                                UUID authorId,
                                                String token) {
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
