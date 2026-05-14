package com.reconix.fraud.dto;

import com.reconix.fraud.domain.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudAlertEvent {
    
    private String alertId;
    private String tenantId;
    private String transactionId;
    private String transactionDescription;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    
    private RiskLevel riskLevel;
    private Double riskScore;
    
    private List<String> triggeredRules;
    private String detectionReason;
    
    private LocalDateTime detectedAt;
}
