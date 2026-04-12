package com.reconix.matching.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record Transaction(
        UUID id,
        String tenantId,
        String source,
        String description,
        BigDecimal amount,
        LocalDate transactionDate,
        String currency,
        String referenceNumber,
        String category
) {

    public String normalizedDescription() {
        return description.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "").trim();
    }

    public String normalizedReferenceNumber() {
        return referenceNumber.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
}