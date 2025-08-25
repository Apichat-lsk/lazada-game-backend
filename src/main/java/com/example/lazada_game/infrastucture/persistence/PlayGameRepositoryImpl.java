package com.example.lazada_game.infrastucture.persistence;

import com.example.lazada_game.domain.model.PlayGame;
import com.example.lazada_game.domain.repository.PlayGameRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlayGameRepositoryImpl implements PlayGameRepository {

    private final PlayGamemongoRepository playGamemongoRepository;

    @Override
    public PlayGame createPlayGame(PlayGame playGame) {
        return playGamemongoRepository.save(playGame);
    }

    @Override
    public Optional<PlayGame> findByUserId(ObjectId userId) {
        return Optional.ofNullable(
                playGamemongoRepository.findFirstByUserIdOrderByGameDateDesc(userId)
        );
    }
}
