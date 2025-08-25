package com.example.lazada_game.application;

import com.example.lazada_game.domain.model.GameTransaction;
import com.example.lazada_game.domain.model.Questions;
import com.example.lazada_game.domain.repository.QuestionsRepository;
import com.example.lazada_game.web.dto.CheckAnswerRequest;
import com.example.lazada_game.web.dto.QuestionsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionsRepository questionsRepository;

    public List<Questions> getQuestionsAll() {
        return questionsRepository.findAll();
    }

    public List<Questions> getByDate(QuestionsRequest questionsRequest) {
        List<Questions> todayQuestions = questionsRepository.findByDate(questionsRequest);
        return todayQuestions;
    }

    public List<Questions> getAnswer(LocalDateTime date) {
        List<Questions> todayQuestions = questionsRepository.findAnswer(date);
        return todayQuestions;
    }

    public Optional<Questions> getById(CheckAnswerRequest request){
        return questionsRepository.findQuestionsById(request);
    }



}
