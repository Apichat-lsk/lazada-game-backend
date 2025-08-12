package com.example.lazada_game.domain.repository;

import com.example.lazada_game.web.dto.BoardRequest;
import com.example.lazada_game.web.dto.BoardResponse;
import org.bson.Document;
import java.util.List;

public interface BoardRepository {
    List<BoardResponse> findAllBoard(BoardRequest request);
}
