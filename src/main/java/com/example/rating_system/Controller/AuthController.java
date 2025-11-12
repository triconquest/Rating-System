package com.example.rating_system.Controller;

import com.example.rating_system.DTO.LoginDto;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if(!user.isEmailConfirmed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email not confirmed, check your confirmation link");
        }

        if(!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Incorrect email or password");
        }

        if(user.getRole() == Role.ROLE_PENDING_SELLER && !user.isApproved())
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("This seller profile is pending admin approval");
        }

        return ResponseEntity.ok("Login successful - welcome, " + user.getFirstName());
    }
}
