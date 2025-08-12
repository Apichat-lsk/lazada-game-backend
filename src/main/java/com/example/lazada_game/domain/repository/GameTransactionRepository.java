package com.example.lazada_game.domain.repository;

import com.example.lazada_game.domain.model.GameTransaction;

import java.time.LocalDate;
import java.util.List;

public interface GameTransactionRepository {
    void createGameTransaction(List<GameTransaction> gameTransaction);

    GameTransaction findByDate(GameTransaction user, LocalDate localDate);
}
