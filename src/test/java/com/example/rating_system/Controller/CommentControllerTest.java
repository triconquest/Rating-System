package com.example.rating_system.Controller;

import com.example.rating_system.DTO.CommentDto;
import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.CommentRepository;
import com.example.rating_system.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // constructor injection
        commentController = new CommentController(commentRepository, userRepository);
    }

    @Test
    void testAddCommentWithAuthor() {
        UUID sellerId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        User seller = new User();
        User author = new User();

        CommentDto dto = new CommentDto();
        dto.setMessage("test message");
        dto.setAuthorId(authorId);

        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));

        Comment savedComment = new Comment();
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        Comment result = commentController.addComment(sellerId, dto);

        assertNotNull(result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testAddCommentSellerNotFound() {
        UUID sellerId = UUID.randomUUID();
        CommentDto dto = new CommentDto();

        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentController.addComment(sellerId, dto);
        });

        assertEquals("Seller not found", exception.getMessage());
    }
}
