package com.example.rating_system.DTO;

import java.util.UUID;

public class SellerRatingDto {

    private UUID sellerId;
    private String sellerName;

    private double averageRating;
    private int totalRatings;

    private String game;

    public SellerRatingDto(UUID sellerId, String sellerName, double averageRating, int totalRatings)
    {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
    }

    public UUID getSellerId() { return sellerId; }
    public String getSellerName() { return sellerName; }
    public double getAverageRating() { return averageRating; }
    public int getTotalRatings() { return totalRatings; }
}
