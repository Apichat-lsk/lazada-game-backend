package com.example.lazada_game.application;

import com.example.lazada_game.domain.model.PlayGame;
import com.example.lazada_game.domain.repository.PlayGameRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayGameService {

    private final PlayGameRepository repository;

    public PlayGame createPlayGame(PlayGame playGame) {
        return repository.createPlayGame(playGame);
    }
    public Optional<PlayGame> getLastGameDate(ObjectId userId){
        return repository.findByUserId(userId);
    }
}
