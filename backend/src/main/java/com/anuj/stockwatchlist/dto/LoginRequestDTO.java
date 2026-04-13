package com.anuj.stockwatchlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {

    @Size(min = 1, max = 30, message = "Username should be atleast 1 and atmax 30 characters")
    private String username;

    @NotBlank(message = "Password should not be blank")
    private String password;

    public LoginRequestDTO(
            @Size(min = 1, max = 30, message = "Username should be atleast 1 and atmax 30 characters") String username,
            @NotBlank(message = "Password should not be blank") String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequestDTO [username=" + username + ", password=" + password + "]";
    }

}
