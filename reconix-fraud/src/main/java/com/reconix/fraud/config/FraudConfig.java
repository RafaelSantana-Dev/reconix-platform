package com.reconix.fraud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "fraud")
public class FraudConfig {
    
    private Rules rules = new Rules();
    private RiskLevels riskLevels = new RiskLevels();
    
    @Data
    public static class Rules {
        private DuplicatePayment duplicatePayment = new DuplicatePayment();
        private UnusualAmount unusualAmount = new UnusualAmount();
        private HighFrequency highFrequency = new HighFrequency();
        private RoundAmount roundAmount = new RoundAmount();
        private UnusualTime unusualTime = new UnusualTime();
    }
    
    @Data
    public static class DuplicatePayment {
        private boolean enabled = true;
        private int timeWindowMinutes = 5;
    }
    
    @Data
    public static class UnusualAmount {
        private boolean enabled = true;
        private double zScoreThreshold = 3.0;
    }
    
    @Data
    public static class HighFrequency {
        private boolean enabled = true;
        private int maxTransactionsPerHour = 50;
    }
    
    @Data
    public static class RoundAmount {
        private boolean enabled = true;
        private double suspiciousThreshold = 0.8;
    }
    
    @Data
    public static class UnusualTime {
        private boolean enabled = true;
        private int businessHoursStart = 6;
        private int businessHoursEnd = 22;
    }
    
    @Data
    public static class RiskLevels {
        private double low = 0.3;
        private double medium = 0.6;
        private double high = 0.8;
        private double critical = 0.95;
    }
}
