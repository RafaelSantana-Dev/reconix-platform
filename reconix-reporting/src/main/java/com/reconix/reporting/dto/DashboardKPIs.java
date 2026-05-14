package com.reconix.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKPIs {
    
    private Long totalTransactions;
    private Long matchedTransactions;
    private Long unmatchedTransactions;
    private Double reconciliationRate;
    
    private BigDecimal totalAmount;
    private BigDecimal matchedAmount;
    private BigDecimal unmatchedAmount;
    
    private Long fraudAlertsCount;
    private Long criticalAlertsCount;
}
