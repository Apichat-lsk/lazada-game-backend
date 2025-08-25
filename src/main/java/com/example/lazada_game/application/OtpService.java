package com.example.lazada_game.application;

import com.example.lazada_game.domain.model.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplateObject;
    private final RedisTemplate<String, String> redisTemplateString;
//    private final JavaMailSender mailSender;
    private final ChannelTopic otpResultTopic;
    private final EmailService emailService;

    @Async
    public void sendOtpAwsService(Users users, String otp) {
        try {
            String keyOtp = "otp:" + users.getEmail();
            String keyUsers = "user:" + users.getEmail();
            redisTemplateString.opsForValue().set(keyOtp, otp, Duration.ofMinutes(5));
            redisTemplateObject.opsForValue().set(keyUsers, users, Duration.ofMinutes(5));
            emailService.sendOtpEmail(users.getEmail(),otp);
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", users.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send OTP");
        }
    }

    public boolean validateOtp(String email, String otpInput) {
        ObjectMapper mapper = new ObjectMapper();
        String keyOtp = "otp:" + email;
        String keyUsers = "user:" + email;
        String correctOtp = redisTemplateString.opsForValue().get(keyOtp);
        System.out.println("otpInput: " + otpInput);
        System.out.println("correctOtp: " + correctOtp);
        if (otpInput.equals(correctOtp)) {
            try {
                Object obj = redisTemplateObject.opsForValue().get(keyUsers);
                if (obj == null) {
                    System.err.println("No user found in Redis for key: " + keyUsers);
                    return false;
                }
                Users users = mapper.convertValue(obj, Users.class);

                System.out.println("Users from Redis: " + users);
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", email);
                payload.put("success", true);
                payload.put("users", users);

                String message = mapper.writeValueAsString(payload); // serialize map to JSON string
                System.out.println("Message sent to Redis topic: " + message);
                redisTemplateObject.convertAndSend(otpResultTopic.getTopic(), message);

                redisTemplateObject.delete(keyUsers);
                redisTemplateString.delete(keyOtp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("success", false);
            try {
                String message = mapper.writeValueAsString(payload);
                redisTemplateObject.convertAndSend(otpResultTopic.getTopic(), message);
                redisTemplateObject.delete(keyUsers);
                redisTemplateString.delete(keyOtp);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean forgotPasswordVerifyOtp(String email, String otp) {
        String keyOtp = "otp:" + email;
        String correctOtp = redisTemplateString.opsForValue().get(keyOtp);
        if (!otp.equals(correctOtp)) {
            return false;
        }
        return true;
    }
}