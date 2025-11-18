package com.example.rating_system.Controller;

import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.CommentStatus;
import com.example.rating_system.Model.User;
import com.example.rating_system.Services.AdminService;
import com.example.rating_system.Services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final CommentService commentService;

    public AdminController(AdminService adminService, CommentService commentService) {
        this.adminService = adminService;
        this.commentService = commentService;
    }

    @GetMapping("/sellers")
    public List<User> getAllSellers() {
        return adminService.getAllSellers();
    }

    @GetMapping("/sellers/pending")
    public List<User> getPendingSellers() {
        return adminService.getPendingSellers();
    }

    @PutMapping("/sellers/{id}/approve")
    public String approveSeller(@PathVariable UUID id) {
        return adminService.approveSeller(id);
    }

    @PutMapping("/sellers/{id}/decline")
    public String declineSeller(@PathVariable UUID id) {
        return adminService.declineSeller(id);
    }

    @GetMapping("/comments/pending")
    public List<Comment> getPendingComments() { return commentService.getPendingComments(); }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> moderateComment(@PathVariable UUID commentId, @RequestParam CommentStatus status)
    {
       return commentService.moderateComment(commentId, status);
    }
}

