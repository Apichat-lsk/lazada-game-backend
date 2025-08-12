package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.Users;
import com.example.lazada_game.domain.repository.AuthRepository;
import com.example.lazada_game.web.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthMongoRepository authMongoRepository;
    @Override
    public Users register(Users users) {
        return authMongoRepository.save(users);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        // ตัวอย่าง return null เฉย ๆ ยังไม่ได้ implement
        return null;
    }

    @Override
    public boolean existsByUsername(String username) {
        return authMongoRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return authMongoRepository.existsByEmail(email);
    }

    @Override
    public Optional<Users> findByUsername(String username) {
        return authMongoRepository.findByUsername(username);
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return authMongoRepository.findByEmail(email);
    }

    @Override
    public Users updateByEmail(Users users) {
        return authMongoRepository.save(users);
    }


}
