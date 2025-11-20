package com.example.rating_system.Services;

import com.example.rating_system.DTO.CommentDto;
import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.CommentStatus;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.CommentRepository;
import com.example.rating_system.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public ResponseEntity<?> addComment(UUID sellerId, CommentDto dto, HttpServletRequest request) {
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));

        if(!seller.isApproved() || seller.getRole() != Role.SELLER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot comment on a pending seller");
        }

        if(dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        Comment comment = new Comment();
        comment.setMessage(dto.getMessage());
        comment.setRating(dto.getRating());
        comment.setSeller(seller);

        HttpSession session = request.getSession(false);

        if(session != null) {
            UUID userId = (UUID) session.getAttribute("sellerId");
            if(userId != null) {
                User author = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
                comment.setAuthor(author);
                comment.setAnonymousToken(null);
            }
        }

        if(comment.getAuthor() == null) {
            String token = UUID.randomUUID().toString();
            comment.setAnonymousToken(token);
            comment.setAuthor(null);
        }

        comment.setStatus(CommentStatus.PENDING);
        Comment saved = commentRepository.save(comment);

        if(saved.getAuthor() == null && saved.getAnonymousToken() != null) {
            return ResponseEntity.ok(Map.of(
                    "commentId", saved.getId(),
                    "anonymousToken", saved.getAnonymousToken()
            ));
        }

        return ResponseEntity.ok(Map.of("commentId", saved.getId()));
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
                                           HttpServletRequest request,
                                           String token)
    {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found."));

        HttpSession session = request.getSession(false);

        UUID authorId = (UUID) session.getAttribute("sellerId");

        // registered user must have a session
        if(comment.getAuthor() != null) {
            UUID userId = (UUID) session.getAttribute("userId");
            if(!comment.getAuthor().getId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own comments");
            }
        } // anonymous user doesn't have a session
        else if(comment.getAnonymousToken() != null) {
            if(!comment.getAnonymousToken().equals(token)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid anonymous token");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment has no author or anonymous token");
        }

        comment.setMessage(dto.getMessage());
        comment.setRating(dto.getRating());
        comment.setStatus(CommentStatus.PENDING); // updated comment needs to be re-approved

        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }

    // get every comment linked to this user
    public List<CommentDto> getApprovedComments(UUID sellerId) {
        return commentRepository.findAllBySellerIdAndStatus(sellerId, CommentStatus.APPROVED)
                .stream()
                .map(CommentDto::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> deleteComment(UUID commentId,
                                                HttpServletRequest request,
                                                String token) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        HttpSession session = request.getSession(false);

        UUID authorId = (UUID) session.getAttribute("sellerId");

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
