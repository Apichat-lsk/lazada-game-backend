package com.example.lazada_game.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "game_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {
    @Id
    private String id;
    private ObjectId user_id;
    private String questions;
    private String inputAnswer;
    private String answer;
    private int score;
    private int time;
    private LocalDateTime createAt;
}
