package com.example.rating_system.DTO;

import jakarta.validation.constraints.NotBlank;

public class GameObjectDto {

    @NotBlank(message = "Game title can't be blank")
    private String title;

    @NotBlank(message = "Text can't be blank")
    private String text;

    public String getTitle() { return title; }
    public String getText() { return text; }

    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
}
