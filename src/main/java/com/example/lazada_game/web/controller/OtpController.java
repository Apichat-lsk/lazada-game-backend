package com.example.lazada_game.web.controller;

import com.example.lazada_game.application.AuthService;
import com.example.lazada_game.application.OtpService;
import com.example.lazada_game.domain.model.Users;
import com.example.lazada_game.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Optional;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;
    private final AuthService authService;
    private final StringRedisTemplate redisTemplate;
    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody @Valid Users users) {
        RegisterResponse message = authService.register(users);
        if (message.getCheck().equals(true)) {
            String otp = generateOtp();
            String key = "otp:" + users.getEmail();
            otpService.saveOtp(users, otp);
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @PostMapping("/send-otp-again")
    public ResponseEntity<?> sendOtpAgain(@RequestBody @Valid Users users) {
        RegisterResponse message = new RegisterResponse();
          try {
              String otp = generateOtp();
              String key = "otp:" + users.getEmail();
              otpService.saveOtp(users, otp);
              message.setMessage("Send OTP again success");
              message.setCheck(true);
              return ResponseEntity.ok(message);
          }catch (Exception e){
              message.setMessage("Send OTP again failed");
              message.setCheck(false);
              return ResponseEntity.ok(message);
          }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpRequest otp) {
        OtpResponse result = new OtpResponse();
        boolean isValid = otpService.validateOtp(otp.getEmail(), otp.getOtp());
        if (isValid) {
            result.setMessage("OTP verified");
            result.setStatus(true);
            return ResponseEntity.ok(result);
        } else {
            result.setMessage("Invalid OTP");
            result.setStatus(false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/get")
    public ResponseEntity<?> getOTP(@RequestBody ForgotPasswordGetOtpRequest request) {
        ForgotPasswordGetOtpResponse result = new ForgotPasswordGetOtpResponse();
        System.out.println("Email received: [" + request.getEmail() + "]");
        Optional<Users> users = authService.checkUsersByEmail(request.getEmail());
        System.out.println("Find user by email :" + users);
        if (users.isEmpty()) {
            result.setMessage("No email in system");
            result.setStatus(false);
            return ResponseEntity.ok(result);
        } else {
            String otp = generateOtp();
            String key = "otp:" + users.get().getEmail();
            otpService.saveOtp(users.get(), otp);
            result.setMessage("Find email");
            result.setStatus(true);
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/verify-otp-forgot-password")
    public ResponseEntity<OtpResponse> verifyOtpForgotPassword(@RequestBody @Valid OtpRequest request){
        OtpResponse result = new OtpResponse();
        if (!otpService.forgotPasswordVerifyOtp(request.getEmail(), request.getOtp())){
            result.setMessage("Verify OTP failed");
            result.setStatus(false);
            return ResponseEntity.ok(result);
        }
        result.setMessage("Verify OTP success");
        result.setStatus(true);
        return ResponseEntity.ok(result);
    }
}
