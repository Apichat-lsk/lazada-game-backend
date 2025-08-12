package com.example.lazada_game.application;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
@Service
public class RecaptchaService {
    private final String SECRET_KEY = "6LeSJ5orAAAAAK6R65cnenrNi21hTEysjBVoQ21e";
    private final RestTemplate restTemplate;

    public RecaptchaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyToken(String token) {
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify" +
                    "?secret=" + SECRET_KEY +
                    "&response=" + token;
            Map response = restTemplate.postForObject(url, null, Map.class);
            if (response == null) return false;
            return (Boolean) response.get("success");
        } catch (Exception e) {
            return false;
        }
    }
}

