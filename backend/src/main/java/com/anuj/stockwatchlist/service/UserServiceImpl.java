package com.anuj.stockwatchlist.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.anuj.stockwatchlist.repository.UserRepository;
import com.anuj.stockwatchlist.dto.LoginRequestDTO;
import com.anuj.stockwatchlist.dto.LoginResponseDTO;
import com.anuj.stockwatchlist.dto.UserRequestDTO;
import com.anuj.stockwatchlist.dto.UserResponseDTO;
import com.anuj.stockwatchlist.exceptions.UserAlreadyExistsException;
import com.anuj.stockwatchlist.exceptions.EmailAlreadyExistsException;
import com.anuj.stockwatchlist.exceptions.InvalidCredentialsException;
import com.anuj.stockwatchlist.models.User;
import com.anuj.stockwatchlist.models.UserPrincipal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Override
    public User findUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("No user found with the id:" + id);
        }
        return user.get();
    }

    @Override
    public LoginResponseDTO register(UserRequestDTO user) {

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
        User savedUser = userRepository.save(theUser);
        String token = jwtService.generateToken(savedUser.getId());

        return new LoginResponseDTO(token, "Bearer", jwtService.getExpirationTime());
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO user) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new InvalidCredentialsException("Incorrect Username or Password");
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String token = jwtService.generateToken(principal.getId());
        return new LoginResponseDTO(token, "Bearer", jwtService.getExpirationTime());

    }

    @Override
    public UserResponseDTO deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserResponseDTO response = mapper.map(user, UserResponseDTO.class);
        userRepository.delete(user);

        return response;
    }

}
