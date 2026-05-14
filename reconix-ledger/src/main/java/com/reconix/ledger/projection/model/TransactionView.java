package com.reconix.ledger.projection.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder(toBuilder = true)
public class TransactionView {
    private String transactionId;
    private String tenantId;
    private String source;
    private String description;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private String currency;
    private String referenceNumber;
    private String category;
    private String status;
    private String reason;
    private String matchInfo;
    private String matchStatus;
    private String divergenceInfo;
    private String fraudInfo;
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, Object> metadata;
}