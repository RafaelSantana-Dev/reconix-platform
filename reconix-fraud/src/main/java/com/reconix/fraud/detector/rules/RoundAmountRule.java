package com.reconix.fraud.detector.rules;

import com.reconix.fraud.config.FraudConfig;
import com.reconix.fraud.detector.FraudRule;
import com.reconix.fraud.dto.MatchFoundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoundAmountRule implements FraudRule {

    private final FraudConfig fraudConfig;

    @Override
    public double evaluate(MatchFoundEvent event) {
        if (!isEnabled()) {
            return 0.0;
        }

        BigDecimal amount = event.getSourceAmount();
        
        // Verifica se o valor é "redondo" (múltiplo de 100, 500, 1000, etc.)
        boolean isRound = isRoundAmount(amount);

        if (isRound && amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            log.debug("Round amount detected: {} for transaction: {}", 
                amount, event.getSourceTransactionId());
            
            double threshold = fraudConfig.getRules().getRoundAmount().getSuspiciousThreshold();
            return threshold;
        }

        return 0.0;
    }

    private boolean isRoundAmount(BigDecimal amount) {
        // Verifica se é múltiplo de 100, 500, 1000, 5000, 10000
        BigDecimal[] roundValues = {
            BigDecimal.valueOf(10000),
            BigDecimal.valueOf(5000),
            BigDecimal.valueOf(1000),
            BigDecimal.valueOf(500),
            BigDecimal.valueOf(100)
        };

        for (BigDecimal roundValue : roundValues) {
            if (amount.remainder(roundValue).compareTo(BigDecimal.ZERO) == 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getRuleName() {
        return "ROUND_AMOUNT";
    }

    @Override
    public boolean isEnabled() {
        return fraudConfig.getRules().getRoundAmount().isEnabled();
    }
}
