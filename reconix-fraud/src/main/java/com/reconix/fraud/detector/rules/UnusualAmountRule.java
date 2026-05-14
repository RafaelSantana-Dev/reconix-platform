package com.reconix.fraud.detector.rules;

import com.reconix.fraud.config.FraudConfig;
import com.reconix.fraud.detector.FraudRule;
import com.reconix.fraud.domain.TransactionStatistics;
import com.reconix.fraud.dto.MatchFoundEvent;
import com.reconix.fraud.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnusualAmountRule implements FraudRule {

    private final FraudConfig fraudConfig;
    private final StatisticsService statisticsService;

    @Override
    public double evaluate(MatchFoundEvent event) {
        if (!isEnabled()) {
            return 0.0;
        }

        TransactionStatistics stats = statisticsService.getStatistics(event.getTenantId());
        
        if (stats == null || stats.getTransactionCount() < 10) {
            // Não há dados suficientes para análise estatística
            return 0.0;
        }

        BigDecimal amount = event.getSourceAmount();
        BigDecimal mean = stats.getAverageAmount();
        BigDecimal stdDev = stats.getStandardDeviation();

        if (stdDev.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        // Calcula o Z-Score
        double zScore = amount.subtract(mean)
            .divide(stdDev, 2, BigDecimal.ROUND_HALF_UP)
            .abs()
            .doubleValue();

        double threshold = fraudConfig.getRules().getUnusualAmount().getZScoreThreshold();

        if (zScore > threshold) {
            log.warn("Unusual amount detected. Z-Score: {} for transaction: {}", 
                zScore, event.getSourceTransactionId());
            
            // Normaliza o score (quanto maior o z-score, maior o risco)
            return Math.min(zScore / (threshold * 2), 1.0);
        }

        return 0.0;
    }

    @Override
    public String getRuleName() {
        return "UNUSUAL_AMOUNT";
    }

    @Override
    public boolean isEnabled() {
        return fraudConfig.getRules().getUnusualAmount().isEnabled();
    }
}
