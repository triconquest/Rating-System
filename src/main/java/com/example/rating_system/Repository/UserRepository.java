package com.example.rating_system.Repository;

import com.example.rating_system.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByMail(String email);
}
