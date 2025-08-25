package com.example.lazada_game.web.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayGameRequest {

    @NotBlank(message = "User ID is required")
    private ObjectId user_id;
}
