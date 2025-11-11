package com.example.rating_system.Repository;

import com.example.rating_system.Model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
    // figured it's better to use this
    Page<Seller> findAll(String name, Pageable pageable);
}
