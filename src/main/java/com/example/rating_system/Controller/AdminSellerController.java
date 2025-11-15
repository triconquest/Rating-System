package com.example.rating_system.Controller;

import com.example.rating_system.Model.User;
import com.example.rating_system.Services.AdminSellerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/sellers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSellerController {

    private final AdminSellerService adminSellerService;

    public AdminSellerController(AdminSellerService adminSellerService) {
        this.adminSellerService = adminSellerService;
    }

    @GetMapping
    public List<User> getAllSellers() {
        return adminSellerService.getAllSellers();
    }

    @GetMapping("/pending")
    public List<User> getPendingSellers() {
        return adminSellerService.getPendingSellers();
    }

    @PutMapping("{id}/approve")
    public String approveSeller(@PathVariable UUID id) {
        return adminSellerService.approveSeller(id);
    }

    @PutMapping("{id}/decline")
    public String declineSeller(@PathVariable UUID id) {
        return adminSellerService.declineSeller(id);
    }
}
