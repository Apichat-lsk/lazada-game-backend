package com.example.lazada_game.controller;
import com.example.lazada_game.application.RecaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(RecaptchaControllerTest.MockRecaptchaConfig.class)
public class RecaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecaptchaService recaptchaService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockRecaptchaConfig {
        @Bean
        public RecaptchaService recaptchaService() {
            return Mockito.mock(RecaptchaService.class);
        }
    }

    @Test
    void verifyRecaptcha_Success() throws Exception {
        String token = "valid-token";

        Mockito.when(recaptchaService.verifyToken(token)).thenReturn(true);

        mockMvc.perform(post("/api/recaptcha/verify-recaptcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("token", token))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Recaptcha success"));
    }

    @Test
    void verifyRecaptcha_Fail() throws Exception {
        String token = "invalid-token";

        Mockito.when(recaptchaService.verifyToken(token)).thenReturn(false);

        mockMvc.perform(post("/api/recaptcha/verify-recaptcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("token", token))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Recaptcha failed"));
    }

    @Test
    void verifyRecaptcha_MissingToken() throws Exception {
        mockMvc.perform(post("/api/recaptcha/verify-recaptcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing token"));
    }
}
