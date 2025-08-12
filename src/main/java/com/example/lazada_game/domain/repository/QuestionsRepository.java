package com.example.lazada_game.domain.repository;

import com.example.lazada_game.domain.model.Questions;

import java.time.LocalDate;
import java.util.List;

public interface QuestionsRepository {
    List<Questions> findAll();

    List<Questions> findByDate(LocalDate date);
}
