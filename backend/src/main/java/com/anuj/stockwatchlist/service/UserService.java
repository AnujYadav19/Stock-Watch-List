package com.anuj.stockwatchlist.service;

import com.anuj.stockwatchlist.dto.LoginRequestDTO;
import com.anuj.stockwatchlist.dto.LoginResponseDTO;
import com.anuj.stockwatchlist.dto.UserRequestDTO;
import com.anuj.stockwatchlist.dto.UserResponseDTO;
import com.anuj.stockwatchlist.models.User;

public interface UserService {
    User findUserById(int id);

    LoginResponseDTO register(UserRequestDTO user);

    LoginResponseDTO login(LoginRequestDTO user);

    UserResponseDTO deleteUser(int id);
}
