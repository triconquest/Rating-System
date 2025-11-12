package com.example.rating_system.Controller;

import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin/sellers")
public class AdminSellerController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/pending")
    public List<User> getPendingSellers(@RequestHeader("X-User-Role") Role role) {
        if(role != Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can view pending sellers");
        return userRepository.findByApprovedFalseAndRole(Role.PENDING_SELLER);
    }

    @PutMapping("{id}/approve")
    public String approveSeller(@PathVariable UUID id, @RequestHeader("X-User-Role") Role role) {
        if(role != Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can approve pending sellers");

        Optional<User> seller = userRepository.findById(id);
        if(seller.isEmpty())
            return "Couldn't find seller";

        seller.get().setApproved(true);
        seller.get().setRole(Role.SELLER);
        userRepository.save(seller.get());
        return "Seller approved";
    }

    @PutMapping("{id}/decline")
    public String declineSeller(@PathVariable UUID id, @RequestHeader("X-User-Role") Role role) {
        if(role != Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can decline pending sellers");

        Optional<User> seller = userRepository.findById(id);
        if(seller.isEmpty())
            return "Couldn't find seller";

        userRepository.delete(seller.get());
        return "Seller declined and removed";
    }
}
