package com.example.lazada_game.domain.repository;

import com.example.lazada_game.domain.model.Questions;
import com.example.lazada_game.web.dto.CheckAnswerRequest;
import com.example.lazada_game.web.dto.QuestionsRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuestionsRepository {
    List<Questions> findAll();

    List<Questions> findByDate(QuestionsRequest questionsRequest);
    List<Questions> findAnswer(LocalDateTime date);

    Optional<Questions> findQuestionsById(CheckAnswerRequest request);
}
