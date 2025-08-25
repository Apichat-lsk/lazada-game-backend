package com.example.lazada_game.service;

import com.example.lazada_game.application.EmailService;
import com.example.lazada_game.application.OtpService;
import com.example.lazada_game.domain.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OtpServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplateObject;
    @Mock
    private ValueOperations<String, Object> valueOpsObject;
    @Mock
    private RedisTemplate<String, String> redisTemplateString;
    @Mock
    private ValueOperations<String, String> valueOpsString;
    @Mock
    private EmailService emailService;
    @Mock
    private org.springframework.data.redis.listener.ChannelTopic otpResultTopic;
    @InjectMocks
    private OtpService otpService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock opsForValue to return the ValueOperations mocks
        when(redisTemplateObject.opsForValue()).thenReturn(valueOpsObject);
        when(redisTemplateString.opsForValue()).thenReturn(valueOpsString);

        // Mock topic name
        when(otpResultTopic.getTopic()).thenReturn("otpResultTopic");

        // ใช้ constructor ใหม่ที่รับ EmailService แทน JavaMailSender
        otpService = new OtpService(redisTemplateObject, redisTemplateString, otpResultTopic, emailService);
    }

    @Test
    void testSaveOtp() {
        Users users = new Users();
        users.setEmail("test@admin.com");
        String otp = "123456";

        // mock Redis set
        doNothing().when(valueOpsString).set(anyString(), anyString(), any());
        doNothing().when(valueOpsObject).set(anyString(), any(), any());

        // mock EmailService
        doNothing().when(emailService).sendOtpEmail(anyString(), anyString());

        // เรียก method ใหม่
        otpService.sendOtpAwsService(users, otp);

        // verify Redis
        verify(valueOpsString).set(eq("otp:" + users.getEmail()), eq(otp), any());
        verify(valueOpsObject).set(eq("user:" + users.getEmail()), eq(users), any());

        // verify EmailService
        verify(emailService).sendOtpEmail(eq(users.getEmail()), eq(otp));
    }

    @Test
    void testValidateOtp_Correct() {
        String email = "test@example.com";
        String otpInput = "123456";

        Users user = new Users();
        user.setEmail(email);

        when(valueOpsString.get("otp:" + email)).thenReturn(otpInput);
        when(valueOpsObject.get("user:" + email)).thenReturn(user);
        when(redisTemplateObject.convertAndSend(anyString(), anyString())).thenReturn(1L);
        when(redisTemplateObject.delete(anyString())).thenReturn(true);
        when(redisTemplateString.delete(anyString())).thenReturn(true);
        boolean result = otpService.validateOtp(email, otpInput);
        assertTrue(result);
        verify(redisTemplateObject).convertAndSend(eq("otpResultTopic"), anyString());
        verify(redisTemplateObject).delete("user:" + email);
        verify(redisTemplateString).delete("otp:" + email);
    }

    @Test
    void testValidateOtp_Incorrect() {
        String email = "test@example.com";
        String otpInput = "wrongOtp";

        when(valueOpsString.get("otp:" + email)).thenReturn("correctOtp");

        when(redisTemplateObject.convertAndSend(anyString(), anyString())).thenReturn(1L);
        when(redisTemplateObject.delete(anyString())).thenReturn(true);
        when(redisTemplateString.delete(anyString())).thenReturn(true);

        boolean result = otpService.validateOtp(email, otpInput);
        assertFalse(result);

        verify(redisTemplateObject).convertAndSend(eq("otpResultTopic"), anyString());
        verify(redisTemplateObject).delete("user:" + email);
        verify(redisTemplateString).delete("otp:" + email);
    }

    @Test
    void testForgotPasswordVerifyOtp_Correct() {
        String email = "test@example.com";
        String otp = "123456";

        when(valueOpsString.get("otp:" + email)).thenReturn(otp);
        assertTrue(otpService.forgotPasswordVerifyOtp(email, otp));
    }

    @Test
    void testForgotPasswordVerifyOtp_Incorrect() {
        String email = "test@example.com";
        String otp = "wrongOtp";

        when(valueOpsString.get("otp:" + email)).thenReturn("correctOtp");
        assertFalse(otpService.forgotPasswordVerifyOtp(email, otp));
    }

}
