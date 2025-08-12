package com.example.lazada_game.service;

import com.example.lazada_game.application.OtpService;
import com.example.lazada_game.domain.model.Users;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OtpServiceTest {

    @Mock
    private RedisTemplate<String,Object> redisTemplateObject;
    @Mock
    private ValueOperations<String,Object> valueOpsObject;
    @Mock
    private RedisTemplate<String, String> redisTemplateString;
    @Mock
    private ValueOperations<String, String> valueOpsString;
    @Mock
    private JavaMailSender mailSender;
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

        // Mock topic name for sending messages
        when(otpResultTopic.getTopic()).thenReturn("otpResultTopic");

        otpService = new OtpService(redisTemplateObject, redisTemplateString, mailSender, otpResultTopic);
    }


    @Test
    void testSaveOtp(){
        Users users = new Users();
        users.setEmail("test@admin.com");
        String otp = "123456";

        doNothing().when(valueOpsString).set(anyString(), anyString(), any());
        doNothing().when(valueOpsObject).set(anyString(), any(), any());
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        otpService.saveOtp(users, otp);

        verify(valueOpsString).set(eq("otp:" + users.getEmail()), eq(otp), any());
        verify(valueOpsObject).set(eq("user:" + users.getEmail()), eq(users), any());
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testValidateOtp_Correct(){
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
    void testValidateOtp_Incorrect(){
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
    void testForgotPasswordVerifyOtp_Correct(){
        String email = "test@example.com";
        String otp = "123456";

        when(valueOpsString.get("otp:" + email)).thenReturn(otp);
        assertTrue(otpService.forgotPasswordVerifyOtp(email, otp));
    }
    @Test
    void testForgotPasswordVerifyOtp_Incorrect(){
        String email = "test@example.com";
        String otp = "wrongOtp";

        when(valueOpsString.get("otp:" + email)).thenReturn("correctOtp");
        assertFalse(otpService.forgotPasswordVerifyOtp(email, otp));
    }

}
