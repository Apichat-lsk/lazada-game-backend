package com.example.lazada_game.web.dto;

import com.example.lazada_game.domain.model.Questions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerRequest {
    private List<AnswerRequest> input;

}
