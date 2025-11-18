package com.example.rating_system.Controller;

import com.example.rating_system.DTO.PasswordResetDto;
import com.example.rating_system.Services.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody PasswordResetDto dto) {
        return passwordResetService.forgotPassword(dto);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDto dto) {
        return passwordResetService.resetPassword(dto);
    }

    @GetMapping("/check_code")
    public ResponseEntity<String> checkCode(@Valid @RequestBody PasswordResetDto dto) {
        return passwordResetService.checkCode(dto);
    }
}
