package com.reconix.ledger.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public sealed interface EventDto permits
    TransactionCreatedEvent,
    TransactionMatchedEvent,
    TransactionUnmatchedEvent,
    DivergenceDetectedEvent,
    FraudSuspectedEvent {

    UUID eventId();
    String eventType();
    String tenantId();
    Instant occurredAt();
    Map<String, Object> metadata();
}