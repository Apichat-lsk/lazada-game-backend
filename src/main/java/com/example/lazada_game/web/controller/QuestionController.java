package com.example.lazada_game.web.controller;


import com.example.lazada_game.application.*;
import com.example.lazada_game.domain.model.GameTransaction;
import com.example.lazada_game.domain.model.PlayGame;
import com.example.lazada_game.domain.model.Questions;
import com.example.lazada_game.web.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private final ActivityLogsService activityLogsService;
    private final PlayGameService playGameService;
    private final DecodeToken decodeToken;

    @PostMapping("/get-questions")
    public ResponseEntity<QuestionsResponse> getAllQuestions(HttpServletRequest request, @RequestBody @Valid QuestionsRequest questionsRequest) {
        QuestionsResponse result = new QuestionsResponse();
        UserTokenInfo userInfo = decodeToken.decodeToken(request);
        try {
            System.out.println("Request : " + questionsRequest);
            List<Questions> data = service.getByDate(questionsRequest);
            System.out.println("Questions :" + data);
            if (data.size() <= 0) {
                result.setMessage("ไม่พบข้อมูลคำถามในช่วงเวลานี้");
            }
            activityLogsService.createActivityLogs(userInfo.getUserId(), userInfo.getEmail(), "User " + userInfo.getEmail() + " Get Questions success", "Questions");
            result.setData(data);
            result.setMessage("คำถามพร้อมใช้งาน");
        } catch (Exception e) {
            activityLogsService.createActivityLogs(userInfo.getUserId(), userInfo.getEmail(), "User " + userInfo.getEmail() + " Get Questions failed", "Questions");
            result.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public ResponseEntity<QuestionAnswerResponse> checkAnswer(HttpServletRequest auth, @RequestBody QuestionAnswerRequest request) {
        QuestionAnswerResponse result = new QuestionAnswerResponse();
        UserTokenInfo userInfo = decodeToken.decodeToken(auth);
        try {
            System.out.println("Request :" + request);
            List<AnswerRequest> input = request.getInput();
            List<Questions> data = service.getAnswer(input.get(0).getQuestionsDate());
            System.out.println("Data :" + data);
            List<Map<String, Boolean>> answerList = new ArrayList<>();
            int score = 0;
            int correct = 0;

            if (data.size() > 0 && input.size() > 0) {
                PlayGame playGame = new PlayGame();
                playGame.setUserId(userInfo.getUserId());
                playGame.setEmail(userInfo.getEmail());
                playGame.setQuestionsDate(input.get(0).getQuestionsDate());
                playGame.setGameDate(input.get(0).getQuestionsDate());
                playGame.setCreateAt(LocalDateTime.now());
                playGameService.createPlayGame(playGame);
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
                            tx.setQuestionsDate(req.getQuestionsDate());
                            tx.setCreateAt(LocalDateTime.now());
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
                result.setAnswerList(answerList);
                result.setCorrect(correct);
                result.setScore(score);
                result.setMessage("คำนวนคะแนนสำเร็จ");
                activityLogsService.createActivityLogs(userInfo.getUserId(), userInfo.getEmail(), "User " + userInfo.getEmail() + " Check Answer Questions success", "Questions");
                return ResponseEntity.ok(result);
            } else {
                result.setAnswerList(answerList);
                result.setCorrect(correct);
                result.setScore(score);
                result.setMessage("ไม่สามารถคำนวนคะแนนได้");
                activityLogsService.createActivityLogs(userInfo.getUserId(), userInfo.getEmail(), "User " + userInfo.getEmail() + " Check Answer Questions failed", "Questions");
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("Error: " + e.getMessage());
            activityLogsService.createActivityLogs(userInfo.getUserId(), userInfo.getEmail(), "User " + userInfo.getEmail() + " Check Answer Questions failed", "Questions");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }

    @PostMapping("check-answer-by-id")
    public ResponseEntity<CheckAnswerResponse> checkAnswerById(HttpServletRequest auth, @RequestBody CheckAnswerRequest request) {
        CheckAnswerResponse result = new CheckAnswerResponse();
        UserTokenInfo userInfo = decodeToken.decodeToken(auth);
        try {
            Questions data = service.getById(request).orElseThrow(() -> new RuntimeException("Question not found"));
            if (request.getInputAnswer().equals(data.getAnswer())) {
                result.setMessage("คำตอบข้อนี้ ถูกต้อง");
                result.setStatus(true);
                result.setAnswer(data.getAnswer());
            } else {
                result.setMessage("คำตอบข้อนี้ ผิด");
                result.setStatus(false);
                result.setAnswer(data.getAnswer());
            }
            activityLogsService.createActivityLogs(userInfo.getUserId(), userInfo.getEmail(), "User " + userInfo.getEmail() + " Check Answer By One Questions", "Questions");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
