package com.anuj.stockwatchlist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {

    @Size(min = 1, max = 30, message = "Username should be atleast 1 and atmax 30 characters")
    private String username;

    @Email(message = "Email should be in proper format")
    private String email;

    @NotBlank(message = "Password should not be blank")
    private String password;

    public UserRequestDTO(
            @Size(min = 1, max = 30, message = "Username should be atleast 1 and atmax 30 characters") String username,
            @Email(message = "Email should be in proper format") String email,
            @NotBlank(message = "Password should not be blank") String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRequestDTO [username=" + username + ", email=" + email + ", password=" + password + "]";
    }

}
