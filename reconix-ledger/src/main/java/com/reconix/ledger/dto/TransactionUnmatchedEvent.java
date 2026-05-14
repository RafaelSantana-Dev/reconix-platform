package com.reconix.ledger.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record TransactionUnmatchedEvent(
        UUID eventId,
        String eventType,
        String tenantId,
        Instant occurredAt,
        Map<String, Object> metadata,
        UUID transactionId,
        String reason
) implements EventDto {

    public TransactionUnmatchedEvent {
        eventType = "TRANSACTION_UNMATCHED";
        occurredAt = occurredAt != null ? occurredAt : Instant.now();
        metadata = metadata != null ? metadata : Map.of();
    }

    public TransactionUnmatchedEvent(UUID transactionId, String tenantId, String reason) {
        this(UUID.randomUUID(), "TRANSACTION_UNMATCHED", tenantId, Instant.now(), Map.of(),
             transactionId, reason);
    }
}