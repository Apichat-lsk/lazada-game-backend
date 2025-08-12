package com.example.lazada_game.web.controller;


import com.example.lazada_game.application.GameTransactionService;
import com.example.lazada_game.domain.model.GameTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameTransactionService gameTransactionService;


    @PostMapping("check-game-date")
    public ResponseEntity<GameTransaction> getUserByDateGameTransaction(@RequestBody GameTransaction userGame) {
        GameTransaction data = gameTransactionService.getUserByDateGameTransaction(userGame, LocalDate.now());
        return ResponseEntity.ok(data);
    }

}
