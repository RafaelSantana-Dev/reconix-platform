package com.reconix.fraud.service;

import com.reconix.fraud.config.FraudConfig;
import com.reconix.fraud.detector.FraudRule;
import com.reconix.fraud.domain.AlertStatus;
import com.reconix.fraud.domain.FraudAlert;
import com.reconix.fraud.domain.RiskLevel;
import com.reconix.fraud.dto.FraudAlertEvent;
import com.reconix.fraud.dto.MatchFoundEvent;
import com.reconix.fraud.producer.RabbitMQProducer;
import com.reconix.fraud.repository.FraudAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final List<FraudRule> fraudRules;
    private final FraudAlertRepository alertRepository;
    private final RabbitMQProducer rabbitMQProducer;
    private final StatisticsService statisticsService;
    private final FraudConfig fraudConfig;

    public void analyzeTransaction(MatchFoundEvent event) {
        log.info("Analyzing transaction for fraud: {}", event.getSourceTransactionId());

        // Atualiza estatísticas
        statisticsService.updateStatistics(event);

        // Verifica se já existe alerta para esta transação
        if (alertRepository.existsByTransactionId(event.getSourceTransactionId())) {
            log.debug("Alert already exists for transaction: {}", event.getSourceTransactionId());
            return;
        }

        // Avalia todas as regras
        List<String> triggeredRules = new ArrayList<>();
        double totalRiskScore = 0.0;
        int rulesEvaluated = 0;

        for (FraudRule rule : fraudRules) {
            if (rule.isEnabled()) {
                double score = rule.evaluate(event);
                if (score > 0) {
                    triggeredRules.add(rule.getRuleName());
                    totalRiskScore += score;
                    log.debug("Rule {} triggered with score: {}", rule.getRuleName(), score);
                }
                rulesEvaluated++;
            }
        }

        // Calcula score médio
        double averageRiskScore = rulesEvaluated > 0 ? totalRiskScore / rulesEvaluated : 0.0;

        // Se alguma regra foi acionada, cria alerta
        if (!triggeredRules.isEmpty() && averageRiskScore > 0.2) {
            RiskLevel riskLevel = determineRiskLevel(averageRiskScore);
            
            FraudAlert alert = createAlert(event, triggeredRules, averageRiskScore, riskLevel);
            FraudAlert savedAlert = alertRepository.save(alert);

            log.warn("Fraud alert created: {} with risk level: {}", savedAlert.getId(), riskLevel);

            // Publica evento de alerta
            publishFraudAlert(savedAlert);
        } else {
            log.debug("No fraud detected for transaction: {}", event.getSourceTransactionId());
        }
    }

    private FraudAlert createAlert(
        MatchFoundEvent event,
        List<String> triggeredRules,
        double riskScore,
        RiskLevel riskLevel
    ) {
        LocalDateTime now = LocalDateTime.now();
        
        return FraudAlert.builder()
            .tenantId(event.getTenantId())
            .transactionId(event.getSourceTransactionId())
            .transactionDescription(event.getSourceDescription())
            .transactionAmount(event.getSourceAmount())
            .transactionDate(event.getSourceDate())
            .riskLevel(riskLevel)
            .riskScore(riskScore)
            .triggeredRules(triggeredRules)
            .detectionReason(buildDetectionReason(triggeredRules))
            .status(AlertStatus.PENDING)
            .detectedAt(now)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private String buildDetectionReason(List<String> triggeredRules) {
        return "Triggered rules: " + String.join(", ", triggeredRules);
    }

    private RiskLevel determineRiskLevel(double riskScore) {
        FraudConfig.RiskLevels levels = fraudConfig.getRiskLevels();
        
        if (riskScore >= levels.getCritical()) {
            return RiskLevel.CRITICAL;
        } else if (riskScore >= levels.getHigh()) {
            return RiskLevel.HIGH;
        } else if (riskScore >= levels.getMedium()) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.LOW;
        }
    }

    private void publishFraudAlert(FraudAlert alert) {
        FraudAlertEvent event = FraudAlertEvent.builder()
            .alertId(alert.getId())
            .tenantId(alert.getTenantId())
            .transactionId(alert.getTransactionId())
            .transactionDescription(alert.getTransactionDescription())
            .transactionAmount(alert.getTransactionAmount())
            .transactionDate(alert.getTransactionDate())
            .riskLevel(alert.getRiskLevel())
            .riskScore(alert.getRiskScore())
            .triggeredRules(alert.getTriggeredRules())
            .detectionReason(alert.getDetectionReason())
            .detectedAt(alert.getDetectedAt())
            .build();

        rabbitMQProducer.publishFraudAlert(event);
    }
}
