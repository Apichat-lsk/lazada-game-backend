package com.example.lazada_game.domain.repository;

import com.example.lazada_game.domain.model.PlayGame;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface PlayGameRepository {

    PlayGame createPlayGame(PlayGame playGame);
    Optional<PlayGame> findByUserId(ObjectId userId);
}
