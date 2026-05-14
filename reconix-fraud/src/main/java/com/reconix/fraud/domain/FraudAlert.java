package com.reconix.fraud.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fraud_alerts")
public class FraudAlert {

    @Id
    private String id;
    
    private String tenantId;
    private String transactionId;
    private String transactionDescription;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    
    private RiskLevel riskLevel;
    private Double riskScore;
    
    private List<String> triggeredRules;
    private String detectionReason;
    
    private AlertStatus status;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private String reviewNotes;
    
    private LocalDateTime detectedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
