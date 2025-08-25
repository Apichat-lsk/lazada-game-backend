package com.example.lazada_game.application;


import com.example.lazada_game.domain.repository.BoardRepository;
import com.example.lazada_game.web.dto.BoardRequest;
import com.example.lazada_game.web.dto.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Cacheable(
            value = "boards",
            key = "#request.date.toString()" // ใช้วันที่เป็น key
    )
    public List<BoardResponse> getAllBoard(BoardRequest request) {
        return boardRepository.findAllBoard(request);
    }
}
