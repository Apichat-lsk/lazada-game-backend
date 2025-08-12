package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthMongoRepository extends MongoRepository<Users, String> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
