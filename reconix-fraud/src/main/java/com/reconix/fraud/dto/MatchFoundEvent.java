package com.reconix.fraud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchFoundEvent {
    
    private String matchId;
    private String tenantId;
    private String sourceTransactionId;
    private String targetTransactionId;
    private Double matchScore;
    private String matchStatus;
    
    private String sourceDescription;
    private BigDecimal sourceAmount;
    private LocalDateTime sourceDate;
    
    private String targetDescription;
    private BigDecimal targetAmount;
    private LocalDateTime targetDate;
    
    private LocalDateTime matchedAt;
}
