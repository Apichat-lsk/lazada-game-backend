package com.example.lazada_game.domain.model;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogs {

    @Id
    private String id;
    private ObjectId user_id;
    private String email;
    private String action;
    private String type;
    private LocalDateTime createAt;
}
