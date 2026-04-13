package com.anuj.stockwatchlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

import com.anuj.stockwatchlist.dto.ApiResponse;
import com.anuj.stockwatchlist.dto.LoginRequestDTO;
import com.anuj.stockwatchlist.dto.LoginResponseDTO;
import com.anuj.stockwatchlist.dto.UserRequestDTO;
import com.anuj.stockwatchlist.dto.UserResponseDTO;
import com.anuj.stockwatchlist.service.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<LoginResponseDTO> register(@Valid @RequestBody UserRequestDTO user) {
        LoginResponseDTO response = userService.register(user);
        return new ApiResponse<>(true, "User registered successfully", response);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO user) {
        LoginResponseDTO response = userService.login(user);
        return new ApiResponse<>(true, "User logged in successfully", response);
    }

    @DeleteMapping("/me")
    public ApiResponse<UserResponseDTO> deleteUser(Authentication authentication) {
        int userId = Integer.parseInt(authentication.getName());
        UserResponseDTO response = userService.deleteUser(userId);
        return new ApiResponse<>(true, "User and all associated stocks deleted successfully", response);
    }

}
