package com.reconix.fraud.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatistics {
    
    private String tenantId;
    private Long transactionCount;
    private BigDecimal averageAmount;
    private BigDecimal standardDeviation;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
}
