package com.example.lazada_game.domain.model;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "play_game")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayGame {

    @Id
    private String id;
    private ObjectId userId;
    private String email;
    private LocalDateTime gameDate;
    private LocalDateTime questionsDate;
    private LocalDateTime createAt;
}
