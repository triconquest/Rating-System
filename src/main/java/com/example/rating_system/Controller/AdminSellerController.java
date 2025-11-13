package com.example.rating_system.Controller;

import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin/sellers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSellerController {

    private final UserRepository userRepository;

    public AdminSellerController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllSellers() {
        return userRepository.findByApprovedTrueAndRole(Role.ROLE_SELLER);
    }

    @GetMapping("/pending")
    public List<User> getPendingSellers() {
        return userRepository.findByApprovedFalseAndRole(Role.ROLE_PENDING_SELLER);
    }

    @PutMapping("{id}/approve")
    public String approveSeller(@PathVariable UUID id) {
        Optional<User> seller = userRepository.findById(id);
        if(seller.isEmpty())
            return "Couldn't find seller";

        seller.get().setApproved(true);
        seller.get().setRole(Role.ROLE_SELLER);
        userRepository.save(seller.get());
        return "Seller approved";
    }

    @PutMapping("{id}/decline")
    public String declineSeller(@PathVariable UUID id) {
        Optional<User> seller = userRepository.findById(id);
        if(seller.isEmpty())
            return "Couldn't find seller";

        userRepository.delete(seller.get());
        return "Seller declined and removed";
    }
}
