package com.example.lazada_game.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerResponse {
    private Number correct;
    private Number score;
    private List<Map<String,Boolean>> answerList;
    private String message;

}
