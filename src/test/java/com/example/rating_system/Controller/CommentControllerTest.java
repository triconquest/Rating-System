package com.example.rating_system.Controller;

import com.example.rating_system.DTO.CommentDto;
import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.CommentRepository;
import com.example.rating_system.Repository.UserRepository;
import com.example.rating_system.Services.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class CommentControllerTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    private CommentService commentService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // constructor injection
        commentService = new CommentService(commentRepository, userRepository);
        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    void testAddCommentWithAuthor() {
        UUID sellerId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        User seller = new User();
        seller.setRole(Role.ROLE_SELLER);
        seller.setApproved(true);

        User author = new User();
        author.setId(authorId);

        CommentDto dto = new CommentDto();
        dto.setMessage("test message");
        dto.setRating(4);

        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));

        Comment savedComment = new Comment();
        savedComment.setId(UUID.randomUUID());
        savedComment.setAnonymousToken(null);
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        ResponseEntity<?> result = commentService.addComment(sellerId, dto, request);

        assertNotNull(result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testAddCommentSellerNotFound() {
        UUID sellerId = UUID.randomUUID();
        CommentDto dto = new CommentDto();

        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.addComment(sellerId, dto, request);
        });

        assertEquals("Seller not found", exception.getMessage());
    }
}
