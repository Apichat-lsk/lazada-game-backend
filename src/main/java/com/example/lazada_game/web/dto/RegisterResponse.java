package com.example.lazada_game.web.dto;

import com.example.lazada_game.domain.model.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String message;
    private Users users;
    private Boolean check;
}
