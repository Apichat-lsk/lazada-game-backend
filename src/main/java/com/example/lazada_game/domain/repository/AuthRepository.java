package com.example.lazada_game.domain.repository;

import com.example.lazada_game.domain.model.Users;
import com.example.lazada_game.web.dto.LoginRequest;

import java.util.Optional;

public interface AuthRepository {
    Users register(Users users);
    String login(LoginRequest loginRequest);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Users updateByEmail(Users users);
}
