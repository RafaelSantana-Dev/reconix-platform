package com.reconix.ledger.eventstore.repository;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Document(collection = "events")
public class EventEntity {
    private UUID eventId;
    private String eventType;
    private String aggregateId;
    private String tenantId;
    private Instant occurredAt;
    private Map<String, Object> eventData;
    private Map<String, Object> metadata;
    private int version;

    // Constructors
    public EventEntity() {}

    public EventEntity(UUID eventId, String eventType, String aggregateId, String tenantId,
                      Instant occurredAt, Map<String, Object> eventData, Map<String, Object> metadata, int version) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.tenantId = tenantId;
        this.occurredAt = occurredAt;
        this.eventData = eventData;
        this.metadata = metadata;
        this.version = version;
    }

    // Getters and setters
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getAggregateId() { return aggregateId; }
    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }

    public Map<String, Object> getEventData() { return eventData; }
    public void setEventData(Map<String, Object> eventData) { this.eventData = eventData; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
}