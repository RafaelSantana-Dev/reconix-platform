package com.reconix.fraud.service;

import com.reconix.fraud.domain.TransactionStatistics;
import com.reconix.fraud.dto.MatchFoundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String STATS_KEY_PREFIX = "fraud:stats:";
    private static final String AMOUNTS_KEY_PREFIX = "fraud:amounts:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    public void updateStatistics(MatchFoundEvent event) {
        String tenantId = event.getTenantId();
        String amountsKey = AMOUNTS_KEY_PREFIX + tenantId;
        
        // Adiciona o valor à lista de valores recentes
        redisTemplate.opsForList().rightPush(amountsKey, event.getSourceAmount());
        redisTemplate.expire(amountsKey, CACHE_TTL);
        
        // Limita a 1000 valores mais recentes
        Long size = redisTemplate.opsForList().size(amountsKey);
        if (size != null && size > 1000) {
            redisTemplate.opsForList().trim(amountsKey, -1000, -1);
        }
        
        // Recalcula estatísticas
        calculateAndCacheStatistics(tenantId);
    }

    public TransactionStatistics getStatistics(String tenantId) {
        String statsKey = STATS_KEY_PREFIX + tenantId;
        TransactionStatistics stats = (TransactionStatistics) redisTemplate.opsForValue().get(statsKey);
        
        if (stats == null) {
            stats = calculateAndCacheStatistics(tenantId);
        }
        
        return stats;
    }

    private TransactionStatistics calculateAndCacheStatistics(String tenantId) {
        String amountsKey = AMOUNTS_KEY_PREFIX + tenantId;
        List<Object> amounts = redisTemplate.opsForList().range(amountsKey, 0, -1);
        
        if (amounts == null || amounts.isEmpty()) {
            return null;
        }

        List<BigDecimal> values = new ArrayList<>();
        for (Object obj : amounts) {
            if (obj instanceof BigDecimal) {
                values.add((BigDecimal) obj);
            }
        }

        if (values.isEmpty()) {
            return null;
        }

        // Calcula média
        BigDecimal sum = values.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal average = sum.divide(
            BigDecimal.valueOf(values.size()), 
            2, 
            RoundingMode.HALF_UP
        );

        // Calcula desvio padrão
        BigDecimal variance = values.stream()
            .map(v -> v.subtract(average).pow(2))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
        
        BigDecimal stdDev = BigDecimal.valueOf(Math.sqrt(variance.doubleValue()));

        // Min e Max
        BigDecimal min = values.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal max = values.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        TransactionStatistics stats = TransactionStatistics.builder()
            .tenantId(tenantId)
            .transactionCount((long) values.size())
            .averageAmount(average)
            .standardDeviation(stdDev)
            .minAmount(min)
            .maxAmount(max)
            .build();

        // Cache por 24 horas
        String statsKey = STATS_KEY_PREFIX + tenantId;
        redisTemplate.opsForValue().set(statsKey, stats, CACHE_TTL);

        return stats;
    }
}
