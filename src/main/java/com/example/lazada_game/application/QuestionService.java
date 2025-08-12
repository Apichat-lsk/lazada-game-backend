package com.example.lazada_game.application;

import com.example.lazada_game.domain.model.GameTransaction;
import com.example.lazada_game.domain.model.Questions;
import com.example.lazada_game.domain.repository.QuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionsRepository questionsRepository;

    public List<Questions> getQuestionsAll() {
        return questionsRepository.findAll();
    }

    public List<Questions> getByDate() {
        List<Questions> todayQuestions = questionsRepository.findByDate(LocalDate.now());
        return todayQuestions;
    }



}
