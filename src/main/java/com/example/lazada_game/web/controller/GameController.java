package com.example.lazada_game.web.controller;


import com.example.lazada_game.application.DecodeToken;
import com.example.lazada_game.application.GameTransactionService;
import com.example.lazada_game.application.PlayGameService;
import com.example.lazada_game.domain.model.GameTransaction;
import com.example.lazada_game.domain.model.PlayGame;
import com.example.lazada_game.web.dto.PlayGameRequest;
import com.example.lazada_game.web.dto.PlayGameResponse;
import com.example.lazada_game.web.dto.UserTokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameTransactionService gameTransactionService;
    private final PlayGameService playGameService;
    private final DecodeToken decodeToken;


    @PostMapping("/check-game-date")
    public ResponseEntity<GameTransaction> getUserByDateGameTransaction(@RequestBody GameTransaction userGame) {
        try {
            GameTransaction data = gameTransactionService.getUserByDateGameTransaction(userGame, LocalDate.now());
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("check-play-game")
    public ResponseEntity<PlayGameResponse> checkPlayGameDate(HttpServletRequest auth) {
        UserTokenInfo userInfo = decodeToken.decodeToken(auth);
        PlayGameResponse result = new PlayGameResponse();
        try {
            Optional<PlayGame> data = playGameService.getLastGameDate(userInfo.getUserId());
            if (data.isEmpty()) {
                result.setGame_date(null);
            } else {
                result.setGame_date(data.get().getGameDate());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
