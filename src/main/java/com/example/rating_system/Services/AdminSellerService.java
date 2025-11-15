package com.example.rating_system.Services;

import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import com.example.rating_system.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AdminSellerService {

    private final UserRepository userRepository;

    public AdminSellerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllSellers() {
        return userRepository.findByApprovedTrueAndRole(Role.ROLE_SELLER);
    }

    public List<User> getPendingSellers() {
        return userRepository.findByApprovedFalseAndRole(Role.ROLE_PENDING_SELLER);
    }

    public String approveSeller(UUID id) {
        Optional<User> seller = userRepository.findById(id);
        if(seller.isEmpty())
            return "Couldn't find seller";

        seller.get().setApproved(true);
        seller.get().setRole(Role.ROLE_SELLER);
        userRepository.save(seller.get());
        return "Seller approved";
    }

    public String declineSeller(UUID id) {
        Optional<User> seller = userRepository.findById(id);
        if(seller.isEmpty())
            return "Couldn't find seller";

        userRepository.delete(seller.get());
        return "Seller declined and removed";
    }
}
