package com.reconix.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
@EnableJpaRepositories(basePackages = "com.reconix.reporting.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.reconix.reporting.repository.elasticsearch")
public class ReconixReportingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReconixReportingApplication.class, args);
    }
}
