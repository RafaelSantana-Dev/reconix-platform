package com.reconix.ledger.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record DivergenceDetectedEvent(
        UUID eventId,
        String eventType,
        String tenantId,
        Instant occurredAt,
        Map<String, Object> metadata,
        UUID transactionAId,
        UUID transactionBId,
        BigDecimal amountDifference,
        String divergenceType,
        String description
) implements EventDto {

    public DivergenceDetectedEvent {
        eventType = "DIVERGENCE_DETECTED";
        occurredAt = occurredAt != null ? occurredAt : Instant.now();
        metadata = metadata != null ? metadata : Map.of();
    }

    public DivergenceDetectedEvent(UUID transactionAId, UUID transactionBId, String tenantId,
                                BigDecimal amountDifference, String divergenceType, String description) {
        this(UUID.randomUUID(), "DIVERGENCE_DETECTED", tenantId, Instant.now(), Map.of(),
             transactionAId, transactionBId, amountDifference, divergenceType, description);
    }
}