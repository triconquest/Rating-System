package com.example.rating_system.Services;

import com.example.rating_system.DTO.PasswordResetDto;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@Transactional
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    public PasswordResetService(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   RedisService redisService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
    }

    public ResponseEntity<String> forgotPassword(PasswordResetDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("This seller doesn't exist"));

        String code = String.format("%06d", new Random().nextInt(999999));

        redisService.setValue("reset:" + dto.getEmail(), code, Duration.ofMinutes(5));

        // email simulation
        System.out.println("Password reset code for " + dto.getEmail() + ": " + code);

        return ResponseEntity.ok("Reset code sent to your email");
    }

    public ResponseEntity<String> resetPassword(PasswordResetDto dto) {
        String storedCode = redisService.getValue("reset:" + dto.getEmail());

        if(storedCode == null)
            return ResponseEntity.status(HttpStatus.GONE).body("Reset code expired");

        if(!storedCode.equals(dto.getCode()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid reset code");

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("Seller not found."));

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        redisService.deleteKey("reset:" + dto.getEmail());
        return ResponseEntity.ok("Password reset successful");
    }

    public ResponseEntity<String> checkCode(PasswordResetDto dto) {
        String storedCode = redisService.getValue("reset:" + dto.getEmail());

        if(storedCode == null)
            return ResponseEntity.status(HttpStatus.GONE).body("Reset code expired");

        if(!storedCode.equals(dto.getCode()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid reset code");

        return ResponseEntity.ok("Code valid");
    }
}
