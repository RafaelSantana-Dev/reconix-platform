package com.reconix.ledger.config;

import com.reconix.ledger.projection.TransactionProjection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LedgerConfig {

    @Bean
    public TransactionProjection transactionProjection() {
        return new TransactionProjection();
    }
}