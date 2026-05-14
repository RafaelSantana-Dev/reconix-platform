package com.reconix.fraud.detector.rules;

import com.reconix.fraud.config.FraudConfig;
import com.reconix.fraud.detector.FraudRule;
import com.reconix.fraud.dto.MatchFoundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnusualTimeRule implements FraudRule {

    private final FraudConfig fraudConfig;

    @Override
    public double evaluate(MatchFoundEvent event) {
        if (!isEnabled()) {
            return 0.0;
        }

        LocalDateTime transactionDate = event.getSourceDate();
        
        if (transactionDate == null) {
            return 0.0;
        }

        int hour = transactionDate.getHour();
        DayOfWeek dayOfWeek = transactionDate.getDayOfWeek();

        int businessStart = fraudConfig.getRules().getUnusualTime().getBusinessHoursStart();
        int businessEnd = fraudConfig.getRules().getUnusualTime().getBusinessHoursEnd();

        // Verifica se é fim de semana
        boolean isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        
        // Verifica se está fora do horário comercial
        boolean isOutsideBusinessHours = hour < businessStart || hour >= businessEnd;

        if (isWeekend || isOutsideBusinessHours) {
            log.debug("Unusual time detected: {} for transaction: {}", 
                transactionDate, event.getSourceTransactionId());
            return 0.5; // Risco médio
        }

        return 0.0;
    }

    @Override
    public String getRuleName() {
        return "UNUSUAL_TIME";
    }

    @Override
    public boolean isEnabled() {
        return fraudConfig.getRules().getUnusualTime().isEnabled();
    }
}
