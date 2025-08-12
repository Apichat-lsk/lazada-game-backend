package com.example.lazada_game.web.controller;

import com.example.lazada_game.application.RecaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/recaptcha")
@RequiredArgsConstructor
public class RecaptchaController {

    private final String SECRET_KEY = "6LeSJ5orAAAAAK6R65cnenrNi21hTEysjBVoQ21e";
    private final RecaptchaService recaptchaService;

    @PostMapping("/verify-recaptcha")
    public ResponseEntity<?> verifyRecaptcha(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing token");
        }
        boolean valid = recaptchaService.verifyToken(token);
        if (valid) {
            return ResponseEntity.ok(Map.of("status", true, "message", "Recaptcha success"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", false, "message", "Recaptcha failed"));
        }
    }


}