package com.example.lazada_game.web.controller;


import com.example.lazada_game.application.GameTransactionService;
import com.example.lazada_game.application.QuestionService;
import com.example.lazada_game.domain.model.GameTransaction;
import com.example.lazada_game.domain.model.Questions;
import com.example.lazada_game.web.dto.AnswerRequest;
import com.example.lazada_game.web.dto.QuestionAnswerRequest;
import com.example.lazada_game.web.dto.QuestionAnswerResponse;
import com.example.lazada_game.web.dto.QuestionsResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor

public class QuestionController {


    private final QuestionService service;
    private final GameTransactionService gameTransactionService;

    @GetMapping
    public ResponseEntity<QuestionsResponse> getAllQuestions() {
        QuestionsResponse result = new QuestionsResponse();
        try {
            List<Questions> data = service.getByDate();
            System.out.println("Questions :" + data);
            if (data.size() <= 0) {
                result.setMessage("Find questions not found");
            }
            result.setData(data);
            result.setMessage("Find questions success");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public ResponseEntity<QuestionAnswerResponse> checkAnswer(@RequestBody QuestionAnswerRequest request) {
        QuestionAnswerResponse result = new QuestionAnswerResponse();
        try {
            System.out.println("Request :" + request);
            List<AnswerRequest> input = request.getInput();
            List<Questions> data = service.getByDate();
            System.out.println("Data :" + data);
            List<Map<String, Boolean>> answerList = new ArrayList<>();
            int score = 0;
            int correct = 0;

            if (data.size() > 0 && input.size() > 0) {
                List<GameTransaction> gameTransactions = request.getInput().stream()
                        .map(req -> {
                            GameTransaction tx = new GameTransaction();
                            tx.setId(req.getId());
                            tx.setUserId(new ObjectId(req.getUser_id()));
                            tx.setQuestions(req.getQuestions());
                            tx.setAnswer(req.getAnswer());
                            tx.setInputAnswer(req.getInputAnswer());
                            tx.setScore(req.getScore());
                            tx.setTime(req.getTime());
                            tx.setCreateAt(new Date());
                            return tx;
                        })
                        .collect(Collectors.toList());
                gameTransactionService.createGameTransaction(gameTransactions);
                for (int i = 0; i < data.size(); i++) {
                    Questions question = data.get(i);
                    AnswerRequest userAnswer = input.get(i);
                    Map<String, Boolean> answerMap = new HashMap<>();
                    if (question.getId().equals(userAnswer.getId()) &&
                            question.getAnswer().equalsIgnoreCase(userAnswer.getInputAnswer())) {
                        System.out.println("userAnswer.getTitleChoice() : " + userAnswer.getTitleChoice());
                        correct++;
                        score += userAnswer.getScore();
                        answerMap.put(userAnswer.getTitleChoice(), true);
                    } else if (question.getId().equals(userAnswer.getId()) &&
                            !question.getAnswer().equalsIgnoreCase(userAnswer.getInputAnswer())) {
                        answerMap.put(userAnswer.getTitleChoice(), false);
                    }
                    answerList.add(answerMap);
                    System.out.println("answerList : " + answerList);
                }
            }
            result.setAnswerList(answerList);
            result.setCorrect(correct);
            result.setScore(score);
            result.setMessage("Calculate score success");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }


}
