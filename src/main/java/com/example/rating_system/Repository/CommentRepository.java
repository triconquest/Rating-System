package com.example.rating_system.Repository;

import com.example.rating_system.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Comment findByAuthor(String authorName);
}
