package com.reconix.fraud.detector.rules;

import com.reconix.fraud.config.FraudConfig;
import com.reconix.fraud.detector.FraudRule;
import com.reconix.fraud.dto.MatchFoundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicatePaymentRule implements FraudRule {

    private final FraudConfig fraudConfig;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public double evaluate(MatchFoundEvent event) {
        if (!isEnabled()) {
            return 0.0;
        }

        String key = buildKey(event);
        String existingValue = redisTemplate.opsForValue().get(key);

        if (existingValue != null) {
            log.warn("Duplicate payment detected for transaction: {}", event.getSourceTransactionId());
            return 1.0; // Máximo risco para pagamento duplicado
        }

        // Armazena a transação no Redis com TTL
        int timeWindowMinutes = fraudConfig.getRules().getDuplicatePayment().getTimeWindowMinutes();
        redisTemplate.opsForValue().set(
            key, 
            event.getSourceTransactionId(), 
            Duration.ofMinutes(timeWindowMinutes)
        );

        return 0.0;
    }

    private String buildKey(MatchFoundEvent event) {
        return String.format("fraud:duplicate:%s:%s:%s",
            event.getTenantId(),
            event.getSourceAmount(),
            event.getSourceDescription()
        );
    }

    @Override
    public String getRuleName() {
        return "DUPLICATE_PAYMENT";
    }

    @Override
    public boolean isEnabled() {
        return fraudConfig.getRules().getDuplicatePayment().isEnabled();
    }
}
