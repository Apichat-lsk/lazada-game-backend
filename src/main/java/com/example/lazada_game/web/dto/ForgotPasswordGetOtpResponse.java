package com.example.lazada_game.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordGetOtpResponse {
    private String message;
    private String otp;
    private Boolean status;
}
