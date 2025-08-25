package com.example.lazada_game.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckAnswerRequest {
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
    private LocalDateTime questionsDate;

}
