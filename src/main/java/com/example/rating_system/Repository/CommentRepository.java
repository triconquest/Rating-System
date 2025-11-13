package com.example.rating_system.Repository;

import com.example.rating_system.Model.Comment;
import com.example.rating_system.Model.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findBySellerId(UUID sellerId);
    List<Comment> findAllByStatus(CommentStatus status);
    List<Comment> findAllBySellerIdAndStatus(UUID sellerId, CommentStatus commentStatus);
}
