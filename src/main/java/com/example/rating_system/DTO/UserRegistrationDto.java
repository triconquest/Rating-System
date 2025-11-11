package com.example.rating_system.DTO;

public class UserRegistrationDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() {return password; }

    public void setFirstName(String newFirstName) { firstName = newFirstName; }
    public void setLastName(String newLastName) { lastName = newLastName; }
    public void setEmail(String newEmail) { email = newEmail; }
    //public void setPasswordHash(String passwordHash) { password = passwordHash; }
}
