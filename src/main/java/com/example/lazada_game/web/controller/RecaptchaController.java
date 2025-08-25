package com.example.lazada_game.web.controller;

import com.example.lazada_game.application.ActivityLogsService;
import com.example.lazada_game.application.DecodeToken;
import com.example.lazada_game.application.RecaptchaService;
import com.example.lazada_game.web.dto.UserTokenInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
    private final DecodeToken decodeToken;
    private final ActivityLogsService activityLogsService;

    @PostMapping("/verify-recaptcha")
    public ResponseEntity<?> verifyRecaptcha(HttpServletRequest request ,@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing token");
        }
        boolean valid = recaptchaService.verifyToken(token);
        UserTokenInfo userInfo = decodeToken.decodeToken(request);
        if (valid) {
            activityLogsService.createActivityLogs(userInfo.getUserId(), userInfo.getEmail(), "User " + userInfo.getEmail() + " Recaptcha success", "Recaptcha");
            return ResponseEntity.ok(Map.of("status", true, "message", "ยืนยันตัวตน สำเร็จ"));
        } else {
            activityLogsService.createActivityLogs(userInfo.getUserId(), userInfo.getEmail(), "User " + userInfo.getEmail() + " Recaptcha failed", "Recaptcha");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", false, "message", "ยืนยันตัวตน ไม่สำเร็จ"));
        }
    }


}