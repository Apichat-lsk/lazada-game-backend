package com.example.lazada_game.config;

import com.example.lazada_game.application.AuthService;
import com.example.lazada_game.application.OtpService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
class TestConfig {

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