package com.anuj.stockwatchlist.service;

import com.anuj.stockwatchlist.dto.UserRequestDTO;
import com.anuj.stockwatchlist.dto.UserResponseDTO;
import com.anuj.stockwatchlist.models.User;

public interface UserService {
    User findUserById(int id);

    UserResponseDTO register(UserRequestDTO user);

    UserResponseDTO login(UserRequestDTO user);

    UserResponseDTO deleteUser(int id);
}
