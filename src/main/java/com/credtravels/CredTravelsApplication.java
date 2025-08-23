package com.credtravels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class CredTravelsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CredTravelsApplication.class, args);
    }
}
