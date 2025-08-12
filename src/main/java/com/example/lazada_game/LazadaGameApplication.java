package com.example.lazada_game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LazadaGameApplication {

    public static void main(String[] args) {

        SpringApplication.run(LazadaGameApplication.class, args);
        System.out.println("Application Start");

    }

}
