package com.example.rating_system.DTO;

public class PasswordResetDto {
    private String email;
    private String code;
    private String newPassword;

    public String getEmail() { return email; }
    public String getCode() { return code; }
    public String getNewPassword() { return newPassword; }

    public void setEmail() { this.email = email; }
    public void setCode() { this.code = code; }
    public void setNewPassword() { this.newPassword = newPassword; }
}
