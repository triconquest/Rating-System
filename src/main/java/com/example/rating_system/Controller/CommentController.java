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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/{sellerId}/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

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
        }

        comment.setStatus(CommentStatus.PENDING);
        return commentRepository.save(comment);
    }

    // admin gets pending comments
    @GetMapping("/pending")
    public List<Comment> getPendingComments() {
        return commentRepository.findAllByStatus(CommentStatus.PENDING);
    }

    // admin approves/declines a comment
    @PutMapping("/{commentId}/status")
    public ResponseEntity<?> moderateComment(@PathVariable UUID commentId, @RequestParam CommentStatus status, @RequestParam UUID userId)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        if(user.getRole() != Role.ADMIN)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can approve or decline comments");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setStatus(status);
        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public List<Comment> getApprovedComments(@PathVariable UUID sellerId) {
        return commentRepository.findAllBySellerIdAndStatus(sellerId, CommentStatus.APPROVED);
    }

}
