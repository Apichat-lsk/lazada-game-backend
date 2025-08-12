package com.example.lazada_game.web.controller;

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

//    @PostMapping("/register")
//    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
//        authService.register(registerRequest);
//        RegisterResponse result = RegisterResponse.builder()
//                .message("Register Success")
//                .build();
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        ChangePasswordResponse result = new ChangePasswordResponse();
        try {
            Users user = authService.changePassword(request);
            if (user != null) {
                result.setMessage("Change password success");
                result.setStatus(true);
            } else {
                result.setMessage("Change password failed");
                result.setStatus(false);
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setStatus(false);
        }
        return ResponseEntity.ok(result);
    }


}
