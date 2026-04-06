package com.anuj.stockwatchlist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anuj.stockwatchlist.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // Boolean helpers make the Service code much cleaner - much faster than
    // fetching the whole user just to see in username or email already exists
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
