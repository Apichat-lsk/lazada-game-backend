package com.example.lazada_game.application;

import com.example.lazada_game.domain.model.GameTransaction;
import com.example.lazada_game.domain.repository.GameTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameTransactionService {

    private final GameTransactionRepository gameTransactionRepository;
    private final ActivityLogsService activityLogsService;

    public void createGameTransaction(List<GameTransaction> gameTransaction) {
        try {
            gameTransactionRepository.createGameTransaction(gameTransaction);
        } catch (Exception e) {
            throw new RuntimeException("ERROR :" + e.getMessage());
        }
    }

    public GameTransaction getUserByDateGameTransaction(GameTransaction user, LocalDate localDate) {
        try {
            return gameTransactionRepository.findByDate(user, localDate);
        } catch (Exception e) {
            throw new RuntimeException("ERROR :" + e.getMessage());
        }
    }


}
