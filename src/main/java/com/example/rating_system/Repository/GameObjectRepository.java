package com.example.rating_system.Repository;

import com.example.rating_system.Model.GameObject;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameObjectRepository extends JpaRepository<GameObject, UUID> {
    Optional<GameObject> findByTitle(String title);
}
