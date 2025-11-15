package com.example.rating_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class GameObjectDto {

    @NotBlank(message = "Game title can't be blank")
    private String title;

    @NotBlank(message = "Text can't be blank")
    private String text;

    @NotNull(message = "sellerId can't be null")
    private UUID sellerId;

    public String getTitle() { return title; }
    public String getText() { return text; }
    public UUID getSellerId() { return sellerId; }

    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setSellerId(UUID sellerId) { this.sellerId = sellerId; }
}
