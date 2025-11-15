package com.example.rating_system.Controller;

import com.example.rating_system.DTO.UserRegistrationDto;
import com.example.rating_system.Services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationDto dto) {
        return registrationService.register(dto);
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam String email, @RequestParam String code) {
        return registrationService.confirmEmail(email, code);
    }
}
