package com.example.rating_system.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank
    private String code;

    @NotBlank(message = "New password cannot be blank")
    private String newPassword;

    public String getEmail() { return email; }
    public String getCode() { return code; }
    public String getNewPassword() { return newPassword; }

    public void setEmail() { this.email = email; }
    public void setCode() { this.code = code; }
    public void setNewPassword() { this.newPassword = newPassword; }
}
