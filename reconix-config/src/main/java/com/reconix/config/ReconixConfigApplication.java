package com.reconix.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ReconixConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReconixConfigApplication.class, args);
    }
}