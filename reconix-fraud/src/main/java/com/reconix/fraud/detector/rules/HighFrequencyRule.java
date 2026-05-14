package com.reconix.fraud.detector.rules;

import com.reconix.fraud.config.FraudConfig;
import com.reconix.fraud.detector.FraudRule;
import com.reconix.fraud.dto.MatchFoundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class HighFrequencyRule implements FraudRule {

    private final FraudConfig fraudConfig;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public double evaluate(MatchFoundEvent event) {
        if (!isEnabled()) {
            return 0.0;
        }

        String key = buildKey(event);
        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            // Primeira transação, define TTL de 1 hora
            redisTemplate.expire(key, Duration.ofHours(1));
        }

        int maxTransactions = fraudConfig.getRules().getHighFrequency().getMaxTransactionsPerHour();

        if (count > maxTransactions) {
            log.warn("High frequency detected: {} transactions in 1 hour for tenant: {}", 
                count, event.getTenantId());
            
            // Normaliza o score
            double excess = (count - maxTransactions) / (double) maxTransactions;
            return Math.min(excess, 1.0);
        }

        return 0.0;
    }

    private String buildKey(MatchFoundEvent event) {
        return String.format("fraud:frequency:%s", event.getTenantId());
    }

    @Override
    public String getRuleName() {
        return "HIGH_FREQUENCY";
    }

    @Override
    public boolean isEnabled() {
        return fraudConfig.getRules().getHighFrequency().isEnabled();
    }
}
