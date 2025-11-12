package com.example.rating_system.Repository;

import com.example.rating_system.Model.Role;
import com.example.rating_system.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // found a better way, prevents nullptr exception
    Optional<User> findByEmail(String email);
    List<User> findByApprovedFalseAndRole(Role role);
}
