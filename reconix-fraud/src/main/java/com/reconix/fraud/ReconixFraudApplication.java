package com.reconix.fraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
@EnableMongoRepositories
public class ReconixFraudApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReconixFraudApplication.class, args);
    }
}
