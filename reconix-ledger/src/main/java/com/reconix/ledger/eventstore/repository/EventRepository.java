package com.reconix.ledger.eventstore.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<EventEntity, String> {
    List<EventEntity> findByAggregateId(String aggregateId);

    List<EventEntity> findByTenantId(String tenantId);

    @Query(value = "{}", sort = "{occurredAt: -1}")
    List<EventEntity> findLatestEvents();

    long count();
}