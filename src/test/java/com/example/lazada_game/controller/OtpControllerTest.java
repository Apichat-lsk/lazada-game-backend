package com.example.lazada_game.controller;

import com.example.lazada_game.application.AuthService;
import com.example.lazada_game.application.OtpService;
import com.example.lazada_game.domain.model.Users;
import com.example.lazada_game.web.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class OtpControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private OtpService otpService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }

        @Bean
        @Primary
        public OtpService otpService() {
            return Mockito.mock(OtpService.class);
        }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @BeforeEach
    void setup() {
        Mockito.reset(authService, otpService);
    }

    @Test
    void testSendOtp_Success() throws Exception {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");

        RegisterResponse registerResponse = RegisterResponse.builder()
                .check(true)
                .message("User registered")
                .users(user)
                .build();

        Mockito.when(authService.register(Mockito.any(Users.class))).thenReturn(registerResponse);

        Mockito.doNothing().when(otpService).saveOtp(Mockito.any(Users.class), Mockito.anyString());

        mockMvc.perform(post("/api/otp/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.check").value(true))
                .andExpect(jsonPath("$.message").value("User registered"));
    }

    @Test
    void testSendOtp_Fail() throws Exception {
        Users user = new Users();
        user.setEmail("fail@example.com");
        user.setUsername("failuser");
        user.setPassword("password123");

        RegisterResponse registerResponse = RegisterResponse.builder()
                .check(false)
                .message("User registration failed")
                .build();

        Mockito.when(authService.register(Mockito.any(Users.class))).thenReturn(registerResponse);

        mockMvc.perform(post("/api/otp/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.check").value(false))
                .andExpect(jsonPath("$.message").value("User registration failed"));
    }

    @Test
    void testVerifyOtp_Valid() throws Exception {
        OtpRequest request = new OtpRequest();
        request.setEmail("test@example.com");
        request.setOtp("123456");

        Mockito.when(otpService.validateOtp(request.getEmail(), request.getOtp())).thenReturn(true);

        mockMvc.perform(post("/api/otp/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("OTP verified"));
    }

    @Test
    void testVerifyOtp_Invalid() throws Exception {
        OtpRequest request = new OtpRequest();
        request.setEmail("test@example.com");
        request.setOtp("000000");

        Mockito.when(otpService.validateOtp(request.getEmail(), request.getOtp())).thenReturn(false);

        mockMvc.perform(post("/api/otp/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Invalid OTP"));
    }
}
