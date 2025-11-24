package com.example.rating_system.Services;

import com.example.rating_system.DTO.SellerRatingDto;
import com.example.rating_system.Model.*;
import com.example.rating_system.Repository.CommentRepository;
import com.example.rating_system.Repository.GameObjectRepository;
import com.example.rating_system.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RatingService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final GameObjectRepository gameObjectRepository;

    public RatingService(CommentRepository commentRepository,
                         UserRepository userRepository,
                         GameObjectRepository gameObjectRepository)
    {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.gameObjectRepository = gameObjectRepository;
    }

    public Double getSellerRating(UUID sellerId) {
        List<Comment> approved = commentRepository.findAllBySellerIdAndStatus(sellerId, CommentStatus.APPROVED);

        if(approved.isEmpty())
            return 0.0;

        return approved.stream().mapToInt(Comment::getRating).average().orElse(0.0);
    }

    public int getSellerRatingCount(UUID sellerId) {
        return commentRepository.findAllBySellerIdAndStatus(sellerId, CommentStatus.APPROVED).size();
    }

    public List<SellerRatingDto> getTopSellers() {
        List<User> sellers = userRepository.findByApprovedTrueAndRole(Role.SELLER);

        return sellers.stream()
                .map(s -> {
                    double avg = getSellerRating(s.getId());
                    int count = getSellerRatingCount(s.getId());
                    String name = Stream.of(s.getFirstName(), s.getLastName())
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(" "));
                    return new SellerRatingDto(s.getId(), name, avg, count);
                })
                .filter(dto -> dto.getTotalRatings() > 0)
                .sorted(Comparator.comparingDouble(SellerRatingDto::getAverageRating).reversed()
                        .thenComparingInt(SellerRatingDto::getTotalRatings))
                .collect(Collectors.toList());
    }

    public List<SellerRatingDto> filterSellers(String game, double minRating, double maxRating) {
        List<User> sellers = gameObjectRepository.findAllByTitle(game)
                .stream().
                map(GameObject::getSeller)
                .distinct()
                .toList();


        return sellers.stream()
                .map(s -> {
                    double avg = getSellerRating(s.getId());
                    int count = getSellerRatingCount(s.getId());
                    String name = Stream.of(s.getFirstName(), s.getLastName())
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(" "));
                    return new SellerRatingDto(s.getId(), name, avg, count);
                })
                .filter(dto -> dto.getAverageRating() >= minRating && dto.getAverageRating() < maxRating)
                .sorted(Comparator.comparingDouble(SellerRatingDto::getAverageRating).reversed())
                .collect(Collectors.toList());
    }

}
