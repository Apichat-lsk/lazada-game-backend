package com.example.lazada_game.web.controller;


import com.example.lazada_game.application.BoardService;
import com.example.lazada_game.web.dto.BoardRequest;
import com.example.lazada_game.web.dto.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @PostMapping
    public ResponseEntity<?> getAllBoard(@RequestBody BoardRequest request) {
        List<BoardResponse> data = boardService.getAllBoard(request);
        System.out.println("Data :" + data);
        return ResponseEntity.ok(data);
    }


}
