package com.example.rating_system.Controller;

import com.example.rating_system.DTO.SellerRatingDto;
import com.example.rating_system.Services.RatingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/seller/{sellerId}")
    public Double getSellerRating(@PathVariable UUID sellerId) {
        return ratingService.getSellerRating(sellerId);
    }

    @GetMapping("/top")
    public List<SellerRatingDto> getTopSellers() {
        return ratingService.getTopSellers();
    }

    @GetMapping("/filter")
    public List<SellerRatingDto> filter(
            @RequestParam String game,
            @RequestParam int min,
            @RequestParam int max
    ) {
        return ratingService.filterSellers(game, min, max);
    }
}
