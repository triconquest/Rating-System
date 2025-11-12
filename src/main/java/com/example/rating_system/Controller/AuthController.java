package com.example.rating_system.Controller;


import com.example.rating_system.DTO.UserRegistrationDto;
import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.UserRepository;
import com.example.rating_system.Services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @PostMapping("/register")
    public String register(@RequestBody UserRegistrationDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());
        user.setEmailConfirmed(false);
        user.setRole(Role.PENDING_SELLER);
        userRepository.save(user);

        String code = UUID.randomUUID().toString();
        redisService.saveCode(user.getEmail(), code);

        System.out.println("Confirmation link: http://localhost:8080/auth/confirm?email=" + user.getEmail() + "&code=" + code);
        return "Registration successful, check your email to confirm";
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam String email, @RequestParam String code) {
        String storedCode = redisService.getCode(email);

        if(storedCode == null)
            return "Confirmation code expired or invalid";

        if(!storedCode.equals(code))
            return "Invalid confirmation code";

        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty())
            return "User not found";

        user.get().setEmailConfirmed(true);
        userRepository.save(user.get());

        redisService.deleteCode(email);
        return "Email confirmed successfully";
    }
}
