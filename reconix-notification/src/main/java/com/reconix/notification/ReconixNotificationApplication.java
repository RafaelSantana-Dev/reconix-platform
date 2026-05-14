package com.reconix.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories
@EnableAsync
public class ReconixNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReconixNotificationApplication.class, args);
    }
}
