package com.reconix.ledger.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record FraudSuspectedEvent(
        UUID eventId,
        String eventType,
        String tenantId,
        Instant occurredAt,
        Map<String, Object> metadata,
        UUID transactionId,
        String fraudType,
        String riskLevel,
        String description
) implements EventDto {

    public FraudSuspectedEvent {
        eventType = "FRAUD_SUSPECTED";
        occurredAt = occurredAt != null ? occurredAt : Instant.now();
        metadata = metadata != null ? metadata : Map.of();
    }

    public FraudSuspectedEvent(UUID transactionId, String tenantId, String fraudType,
                            String riskLevel, String description) {
        this(UUID.randomUUID(), "FRAUD_SUSPECTED", tenantId, Instant.now(), Map.of(),
             transactionId, fraudType, riskLevel, description);
    }
}