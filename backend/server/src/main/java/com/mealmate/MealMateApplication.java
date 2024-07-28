package com.mealmate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableCaching
@EnableScheduling
public class MealMateApplication {
    public static void main(String[] args) {
        SpringApplication.run(MealMateApplication.class, args);
        log.info("server started");
    }
}
