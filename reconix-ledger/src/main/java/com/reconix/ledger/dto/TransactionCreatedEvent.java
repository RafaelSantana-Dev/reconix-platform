package com.reconix.ledger.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record TransactionCreatedEvent(
        UUID eventId,
        String eventType,
        String tenantId,
        Instant occurredAt,
        Map<String, Object> metadata,
        UUID transactionId,
        String source,
        String description,
        BigDecimal amount,
        LocalDate transactionDate,
        String currency,
        String referenceNumber,
        String category
) implements EventDto {

    public TransactionCreatedEvent {
        eventType = "TRANSACTION_CREATED";
        occurredAt = occurredAt != null ? occurredAt : Instant.now();
        metadata = metadata != null ? metadata : Map.of();
    }

    public TransactionCreatedEvent(UUID transactionId, String tenantId, String source, String description,
                                  BigDecimal amount, LocalDate transactionDate, String currency,
                                  String referenceNumber, String category) {
        this(UUID.randomUUID(), "TRANSACTION_CREATED", tenantId, Instant.now(), Map.of(),
             transactionId, source, description, amount, transactionDate, currency, referenceNumber, category);
    }
}