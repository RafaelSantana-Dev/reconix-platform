package com.reconix.ledger.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record TransactionMatchedEvent(
        UUID eventId,
        String eventType,
        String tenantId,
        Instant occurredAt,
        Map<String, Object> metadata,
        UUID transactionAId,
        UUID transactionBId,
        double similarityScore,
        String matchStatus
) implements EventDto {

    public TransactionMatchedEvent {
        eventType = "TRANSACTION_MATCHED";
        occurredAt = occurredAt != null ? occurredAt : Instant.now();
        metadata = metadata != null ? metadata : Map.of();
    }

    public TransactionMatchedEvent(UUID transactionAId, UUID transactionBId, String tenantId,
                                 double similarityScore, String matchStatus) {
        this(UUID.randomUUID(), "TRANSACTION_MATCHED", tenantId, Instant.now(), Map.of(),
             transactionAId, transactionBId, similarityScore, matchStatus);
    }
}