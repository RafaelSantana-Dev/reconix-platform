package com.reconix.ledger.eventstore;

import com.reconix.ledger.dto.EventDto;
import com.reconix.ledger.domain.AggregateRoot;

import java.util.List;
import java.util.UUID;

public interface EventStore {
    void saveEvent(EventDto event);

    <T extends AggregateRoot> T getAggregateById(String aggregateId, Class<T> aggregateClass);

    List<EventDto> getEventsByAggregateId(String aggregateId);

    List<EventDto> getEventsByTenant(String tenantId);

    List<EventDto> getAllEvents();

    void appendEvents(String aggregateId, List<EventDto> events);

    long getEventCount();
}