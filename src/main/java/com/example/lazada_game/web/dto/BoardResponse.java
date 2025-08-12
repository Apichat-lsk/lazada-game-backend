package com.example.lazada_game.web.dto;

import com.example.lazada_game.web.util.ObjectIdJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId user_id;
    private int score;
    private String username;
    private String email;
}
