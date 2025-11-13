package com.example.rating_system.Controller;

import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.CommentStatus;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.CommentRepository;
import com.example.rating_system.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private User seller;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        commentRepository.deleteAll();

        seller = new User();
        seller.setFirstName("John");
        seller.setLastName("Doe");
        seller.setEmail("seller@example.com");
        seller.setPasswordHash("password");
        seller.setRole(Role.ROLE_SELLER);
        seller.setApproved(true);
        userRepository.save(seller);
    }

    @Test
    void testAddComment() throws Exception {
        String commentJson = """
                {
                    "message": "test comment"
                }
                """;

        mockMvc.perform(post("/users/{sellerId}/comments", seller.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("test comment"))
                .andExpect(jsonPath("$.seller.id").value(seller.getId().toString()));

        List<Comment> comments = commentRepository.findAllBySellerIdAndStatus(seller.getId(), CommentStatus.PENDING);
        assertEquals(1, comments.size());
        assertEquals("test comment", comments.get(0).getMessage());
    }

}
