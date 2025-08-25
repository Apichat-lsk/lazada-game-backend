package com.example.lazada_game.web.util;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimiter {

    private final StringRedisTemplate redis;

    /**
     * จำกัดจำนวนครั้งของ action ต่อช่วงเวลา
     *
     * @param key       unique key เช่น customerId หรือ IP
     * @param limit     จำนวนครั้งสูงสุดที่อนุญาต
     * @param windowSec ระยะเวลาหน่วง (วินาที)
     * @return true = อนุญาต, false = block
     */
    public boolean allow(String key, int limit, int windowSec) {
        String redisKey = "rate:" + key;

        Boolean first = redis.opsForValue()
                .setIfAbsent(redisKey, "1", Duration.ofSeconds(windowSec));

        if (Boolean.TRUE.equals(first)) {
            return true; // ครั้งแรก -> อนุญาต
        }

        Long count = redis.opsForValue().increment(redisKey);

        return count != null && count <= limit;
    }
}