package com.reconix.ledger.eventstore.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reconix.ledger.dto.EventDto;
import com.reconix.ledger.domain.AggregateRoot;
import com.reconix.ledger.domain.TransactionAggregate;
import com.reconix.ledger.eventstore.EventStore;
import com.reconix.ledger.eventstore.repository.EventEntity;
import com.reconix.ledger.eventstore.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoEventStore implements EventStore {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void saveEvent(EventDto event) {
        log.debug("Saving event: {} with ID: {} for tenant: {}",
                  event.eventType(), event.eventId(), event.tenantId());

        try {
            // Serialize the event data to store in MongoDB
            Map<String, Object> eventData = objectMapper.convertValue(event, Map.class);

            EventEntity entity = new EventEntity(
                event.eventId(),
                event.eventType(),
                event.eventId().toString(), // Using event ID as aggregate ID for now
                event.tenantId(),
                event.occurredAt(),
                eventData,
                event.metadata(),
                1 // Simple versioning - in a real implementation, this would be calculated
            );

            eventRepository.save(entity);
            log.debug("Event saved successfully: {}", event.eventId());
        } catch (Exception e) {
            log.error("Error saving event: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving event", e);
        }
    }

    @Override
    public <T extends AggregateRoot> T getAggregateById(String aggregateId, Class<T> aggregateClass) {
        log.debug("Loading aggregate {} of type {}", aggregateId, aggregateClass.getSimpleName());

        List<EventDto> events = getEventsByAggregateId(aggregateId);

        if (events.isEmpty()) {
            log.debug("No events found for aggregate: {}", aggregateId);
            return null;
        }

        // Create a new instance of the aggregate
        T aggregate = createAggregateInstance(aggregateClass, aggregateId);

        // Replay all events to rebuild the aggregate state
        for (EventDto event : events) {
            aggregate.apply(event);
        }

        log.debug("Loaded aggregate {} with {} events applied", aggregateId, events.size());
        return aggregate;
    }

    @Override
    public List<EventDto> getEventsByAggregateId(String aggregateId) {
        log.debug("Fetching events for aggregate: {}", aggregateId);

        List<EventEntity> entities = eventRepository.findByAggregateId(aggregateId);
        return entities.stream()
            .map(this::deserializeEventEntity)
            .sorted(Comparator.comparing(EventDto::occurredAt)) // Sort by occurrence time
            .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getEventsByTenant(String tenantId) {
        log.debug("Fetching events for tenant: {}", tenantId);

        List<EventEntity> entities = eventRepository.findByTenantId(tenantId);
        return entities.stream()
            .map(this::deserializeEventEntity)
            .sorted(Comparator.comparing(EventDto::occurredAt))
            .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getAllEvents() {
        log.debug("Fetching all events");

        List<EventEntity> entities = eventRepository.findLatestEvents();
        return entities.stream()
            .map(this::deserializeEventEntity)
            .sorted(Comparator.comparing(EventDto::occurredAt))
            .collect(Collectors.toList());
    }

    @Override
    public void appendEvents(String aggregateId, List<EventDto> events) {
        log.debug("Appending {} events to aggregate: {}", events.size(), aggregateId);

        for (EventDto event : events) {
            saveEvent(event);
        }
    }

    @Override
    public long getEventCount() {
        return eventRepository.count();
    }

    private <T extends AggregateRoot> T createAggregateInstance(Class<T> aggregateClass, String aggregateId) {
        try {
            if (aggregateClass == TransactionAggregate.class) {
                return aggregateClass.getDeclaredConstructor(String.class).newInstance(aggregateId);
            } else {
                return aggregateClass.getDeclaredConstructor(String.class).newInstance(aggregateId);
            }
        } catch (Exception e) {
            log.error("Error creating aggregate instance: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating aggregate instance", e);
        }
    }

    private EventDto deserializeEventEntity(EventEntity entity) {
        try {
            // Based on the event type, reconstruct the appropriate EventDto
            return switch (entity.getEventType()) {
                case "TRANSACTION_CREATED" -> objectMapper.convertValue(
                    entity.getEventData(), TransactionCreatedEvent.class);
                case "TRANSACTION_MATCHED" -> objectMapper.convertValue(
                    entity.getEventData(), TransactionMatchedEvent.class);
                case "TRANSACTION_UNMATCHED" -> objectMapper.convertValue(
                    entity.getEventData(), TransactionUnmatchedEvent.class);
                case "DIVERGENCE_DETECTED" -> objectMapper.convertValue(
                    entity.getEventData(), DivergenceDetectedEvent.class);
                case "FRAUD_SUSPECTED" -> objectMapper.convertValue(
                    entity.getEventData(), FraudSuspectedEvent.class);
                default -> throw new IllegalArgumentException("Unknown event type: " + entity.getEventType());
            };
        } catch (Exception e) {
            log.error("Error deserializing event entity: {}", e.getMessage(), e);
            throw new RuntimeException("Error deserializing event entity", e);
        }
    }
}