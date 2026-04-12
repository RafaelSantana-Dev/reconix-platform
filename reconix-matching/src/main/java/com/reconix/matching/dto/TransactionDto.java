package com.reconix.matching.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
public record TransactionDto(
    UUID id, String tenantId, String source, String description,
    BigDecimal amount, LocalDate transactionDate, String currency,
    String referenceNumber, String category
) {}
