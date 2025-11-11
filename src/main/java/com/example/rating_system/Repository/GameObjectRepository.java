package com.example.rating_system.Repository;

import com.example.rating_system.Model.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameObjectRepository extends JpaRepository<GameObject, UUID> {
    GameObject findByTitle(String title);
}
