package com.example.rating_system.Controller;

import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("example@gmail.com");
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user.setEmailConfirmed(true);
        user.setApproved(true);
        user.setRole(Role.SELLER);

        userRepository.save(user);
    }

    @Test
    void testLoginSuccess() throws Exception {
        String json = """
                {
                    "email": "example@gmail.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login successful")));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        String json = """
                {
                    "email": "example@gmail.com",
                    "password": "wrongpass"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Incorrect email or password"));
    }

    @Test
    void testLoginEmailNotConfirmed() throws Exception {
        user.setEmailConfirmed(false);
        userRepository.save(user);

        String json = """
                {
                    "email": "example@gmail.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Email not confirmed, check your confirmation link"));
    }
}
