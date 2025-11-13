package com.example.rating_system.Controller;

import com.example.rating_system.DTO.LoginDto;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.UserRepository;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(userRepository, passwordEncoder);
    }

    @Test
    void testLoginSuccessful() {
        LoginDto dto = new LoginDto();
        dto.setEmail("example@gmail.com");
        dto.setPassword("password123");

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(dto.getEmail());
        user.setPasswordHash("encodedPass");
        user.setEmailConfirmed(true);
        user.setApproved(true);
        user.setRole(Role.ROLE_SELLER);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())).thenReturn(true);

        ResponseEntity<String> response = authController.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Login successful"));
        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(passwordEncoder, times(1)).matches(dto.getPassword(), user.getPasswordHash());
    }

    @Test
    void testLoginInvalidPassword() {
        LoginDto dto = new LoginDto();
        dto.setEmail("example@gmail.com");
        dto.setPassword("wrongpass");

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail(dto.getEmail());
        user.setPasswordHash("hashedWrongPass");
        user.setEmailConfirmed(true);
        user.setRole(Role.ROLE_SELLER);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        ResponseEntity<String> response = authController.login(dto);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Incorrect email or password", response.getBody());
    }
}
