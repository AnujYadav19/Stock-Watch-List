package com.anuj.stockwatchlist.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.anuj.stockwatchlist.repository.UserRepository;
import com.anuj.stockwatchlist.dto.UserRequestDTO;
import com.anuj.stockwatchlist.dto.UserResponseDTO;
import com.anuj.stockwatchlist.exceptions.UserAlreadyExistsException;
import com.anuj.stockwatchlist.exceptions.EmailAlreadyExistsException;
import com.anuj.stockwatchlist.exceptions.InvalidCredentialsException;
import com.anuj.stockwatchlist.models.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public User findUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("No user found with the id:" + id);
        }
        return user.get();
    }

    @Override
    public UserResponseDTO register(UserRequestDTO user) {

        String username = user.getUsername();
        String email = user.getEmail();
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username already exists:" + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists:" + email);
        }
        User theUser = mapper.map(user, User.class);
        theUser.setRole("ROLE_USER");
        theUser.setPassword(passwordEncoder.encode(theUser.getPassword()));
        userRepository.save(theUser);
        return mapper.map(theUser, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO login(UserRequestDTO user) {
        String username = user.getUsername();

        Optional<User> dbUser = userRepository.findByUsername(username);

        if (dbUser.isEmpty()) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return mapper.map(dbUser, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO deleteUser(int id) {
        // 1. Find the user first (or throw exception)
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // 2. Map to DTO before deleting so we can return the data
        UserResponseDTO response = mapper.map(user, UserResponseDTO.class);

        // 3. Delete from DB (Cascade will handle the stocks automatically)
        userRepository.delete(user);

        return response;
    }

}
