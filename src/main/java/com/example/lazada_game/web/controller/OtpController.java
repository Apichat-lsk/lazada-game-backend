package com.example.lazada_game.web.controller;

import com.example.lazada_game.application.ActivityLogsService;
import com.example.lazada_game.application.AuthService;
import com.example.lazada_game.application.OtpService;
import com.example.lazada_game.domain.model.Users;
import com.example.lazada_game.web.dto.*;
import com.example.lazada_game.web.util.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
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
    private final ActivityLogsService activityLogsService;
    private final RateLimiter rateLimiter;
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
            otpService.sendOtpAwsService(users, otp);
            activityLogsService.createActivityLogs(null, users.getEmail(), "Send OTP " + otp + " to Email " + users.getEmail(), "Send OTP");
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
            message.setMessage("ส่ง OTP สำเร็จ");
            message.setCheck(true);
            otpService.sendOtpAwsService(users, otp);
            activityLogsService.createActivityLogs(null, users.getEmail(), " Send OTP Agian " + otp + " to Email " + " " + users.getEmail(),
                    "Send OTP Agian");
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            message.setMessage("ส่ง OTP ไม่สำเร็จ");
            message.setCheck(false);
            return ResponseEntity.ok(message);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpRequest otp) {
        OtpResponse result = new OtpResponse();
        boolean isValid = otpService.validateOtp(otp.getEmail(), otp.getOtp());
        if (isValid) {
            result.setMessage("ตรวจสอบ OTP สำเร็จ");
            result.setStatus(true);
            activityLogsService.createActivityLogs(null,otp.getEmail(),
                    "Email " + otp.getEmail() + " Verify OTP " + otp.getOtp() + " Success", "Verify OTP");
            return ResponseEntity.ok(result);
        } else {
            result.setMessage("หมายเลข OTP ไม่ถูกต้อง");
            result.setStatus(false);
            activityLogsService.createActivityLogs(null,otp.getEmail(),
                    "Email " + otp.getEmail() + " Verify OTP " + otp.getOtp() + " Failed", "Verify OTP");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/get")
    public ResponseEntity<?> getOTP(@RequestBody ForgotPasswordGetOtpRequest request) {
        // จำกัดไม่เกิน 5 ครั้งใน 1 นาที
        boolean allowed = rateLimiter.allow("forgot-otp:" + request.getEmail(), 5, 60);
        System.out.println("allowed :" + allowed);
        if (!allowed) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("ขอ OTP เกิน limit แล้ว กรุณาลองใหม่ภายหลัง");
        }
        ForgotPasswordGetOtpResponse result = new ForgotPasswordGetOtpResponse();
        System.out.println("Email received: [" + request.getEmail() + "]");
        Optional<Users> users = authService.checkUsersByEmail(request.getEmail());
        System.out.println("Find user by email :" + users);
        if (users.isEmpty()) {
            result.setMessage("ไม่มี อีเมล นี้ในระบบ");
            result.setStatus(false);
            activityLogsService.createActivityLogs(null,request.getEmail(),
                    "Email " + request.getEmail() + " Forgot Password Send OTP " + null + " Failed", "Forgot Password Send OTP");
            return ResponseEntity.ok(result);
        } else {
            String otp = generateOtp();
            String key = "otp:" + users.get().getEmail();
            otpService.sendOtpAwsService(users.get(), otp);
            result.setMessage("ส่ง OTP สำเร็จ");
            result.setStatus(true);
            activityLogsService.createActivityLogs(null,request.getEmail(),
                    "Email " + request.getEmail() + " Forgot Password Send OTP " + otp + " Success", "Forgot Password Send OTP");
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/verify-otp-forgot-password")
    public ResponseEntity<OtpResponse> verifyOtpForgotPassword(@RequestBody @Valid OtpRequest request) {
        OtpResponse result = new OtpResponse();
        if (!otpService.forgotPasswordVerifyOtp(request.getEmail(), request.getOtp())) {
            result.setMessage("หมายเลข OTP ไม่ถูกต้อง");
            result.setStatus(false);
            activityLogsService.createActivityLogs(null,request.getEmail(),
                    "Email " + request.getEmail() + " Forgot Password Verify OTP " + request.getOtp() + "Failed",
                    "Forgot Password Verify OTP");
            return ResponseEntity.ok(result);
        }
        result.setMessage("ตรวจสอบ OTP สำเร็จ");
        result.setStatus(true);
        activityLogsService.createActivityLogs(null,request.getEmail(),
                "Email " + request.getEmail() + " Forgot Password Verify OTP " + request.getOtp() + "Success",
                "Forgot Password Verify OTP");
        return ResponseEntity.ok(result);
    }
}
