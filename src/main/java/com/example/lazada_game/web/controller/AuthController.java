package com.example.lazada_game.web.controller;

import com.example.lazada_game.application.ActivityLogsService;
import com.example.lazada_game.application.AuthService;
import com.example.lazada_game.domain.model.Users;
import com.example.lazada_game.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ActivityLogsService activityLogsService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    @PostMapping("/check-duplicate")
    public ResponseEntity<?> checkDuplicate(@RequestBody @Valid Users users){
        RegisterResponse message = authService.checkDuplicate(users);
        if (message.getCheck().equals(true)) {
            activityLogsService.createActivityLogs(null, users.getEmail(), "Check Duplicate Email & Username " + users.getEmail() + " & " + users.getUsername(), "Check Duplicate");
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        ChangePasswordResponse result = new ChangePasswordResponse();
        try {
            Users user = authService.changePassword(request);
            if (user != null) {
                result.setMessage("เปลี่ยนรหัสผ่านสำเร็จ");
                result.setStatus(true);
            } else {
                result.setMessage("เปลี่ยนรหัสผ่านไม่สำเร็จ");
                result.setStatus(false);
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setStatus(false);
        }
        return ResponseEntity.ok(result);
    }


}
