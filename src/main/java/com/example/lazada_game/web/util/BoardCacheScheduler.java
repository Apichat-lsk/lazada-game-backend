package com.example.lazada_game.web.util;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;

public class BoardCacheScheduler {
    // เคลียร์ cache ทุกวันเวลา 20:00
    @Scheduled(cron = "0 0 20 * * ?")
    @CacheEvict(value = "boards", allEntries = true)
    public void clearBoardCacheAtNight() {
        System.out.println("Cleared boards cache at 20:00");
    }
}
