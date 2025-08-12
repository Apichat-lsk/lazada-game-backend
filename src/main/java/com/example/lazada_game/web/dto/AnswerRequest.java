package com.example.lazada_game.web.dto;

import com.example.lazada_game.domain.model.Questions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {

    private String id;
    private String user_id;
    private String titleChoice;
    private String inputAnswer;
    private String answer;
    private String questions;
    private boolean checkAnswer;
    private int questionsNumber;
    private int score;
    private int time;

}
