package com.example.lazada_game.application;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
public class RecaptchaService {
    private final String SECRET_KEY = "6LeQJ5orAAAAAMWMB0-ApSLqhYBY_b43Eplm-WnK";
    private final RestTemplate restTemplate;

    public RecaptchaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyToken(String token) {
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";

            // สร้าง body เป็น form-data
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("secret", SECRET_KEY);
            body.add("response", token);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> response = responseEntity.getBody();

            if (response == null)
                return false;

            boolean success = (Boolean) response.get("success");
            // สามารถ log ข้อมูล debug ได้
            System.out.println("reCAPTCHA verify response: " + response);

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
