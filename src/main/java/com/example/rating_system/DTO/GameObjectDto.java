package com.example.rating_system.DTO;

import java.util.UUID;

public class GameObjectDto {
    private String title;
    private String text;
    private UUID sellerId;

    public String getTitle() { return title; }
    public String getText() { return text; }
    public UUID getSellerId() { return sellerId; }

    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setSellerId(UUID sellerId) { this.sellerId = sellerId; }
}
