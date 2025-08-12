package com.example.lazada_game.web.dto;


import com.example.lazada_game.domain.model.Questions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsResponse {
    private List<Questions> data;
    private String message;
}
