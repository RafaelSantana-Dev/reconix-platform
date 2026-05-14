package com.reconix.ledger.service.impl;

import com.reconix.ledger.dto.EventDto;
import com.reconix.ledger.eventstore.EventStore;
import com.reconix.ledger.projection.TransactionProjection;
import com.reconix.ledger.projection.model.TransactionView;
import com.reconix.ledger.service.EventStatistics;
import com.reconix.ledger.service.LedgerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultLedgerService implements LedgerService {

    private final EventStore eventStore;
    private final TransactionProjection transactionProjection;

    @Override
    public void registerEvent(EventDto event) {
        log.debug("Registering event: {} for tenant: {}", event.eventType(), event.tenantId());

        // Save the event to the event store
        eventStore.saveEvent(event);

        // Update the projection
        transactionProjection.handle(event);

        log.info("Event registered successfully: {} with ID: {}", event.eventType(), event.eventId());
    }

    @Override
    public void registerEvents(List<EventDto> events) {
        log.debug("Registering {} events", events.size());

        for (EventDto event : events) {
            registerEvent(event);
        }

        log.info("Successfully registered {} events", events.size());
    }

    @Override
    public List<EventDto> getTransactionEvents(String transactionId) {
        log.debug("Getting events for transaction: {}", transactionId);
        return eventStore.getEventsByAggregateId(transactionId);
    }

    @Override
    public List<EventDto> getTenantEvents(String tenantId) {
        log.debug("Getting events for tenant: {}", tenantId);
        return eventStore.getEventsByTenant(tenantId);
    }

    @Override
    public TransactionView getTransactionState(String transactionId) {
        log.debug("Getting state for transaction: {}", transactionId);
        return transactionProjection.getTransactionById(transactionId);
    }

    @Override
    public List<TransactionView> getTenantTransactions(String tenantId) {
        log.debug("Getting all transactions for tenant: {}", tenantId);
        return transactionProjection.getTransactionsByTenant(tenantId);
    }

    @Override
    public List<TransactionView> getMatchedTransactions(String tenantId) {
        log.debug("Getting matched transactions for tenant: {}", tenantId);
        return transactionProjection.getMatchedTransactions(tenantId);
    }

    @Override
    public List<TransactionView> getUnmatchedTransactions(String tenantId) {
        log.debug("Getting unmatched transactions for tenant: {}", tenantId);
        return transactionProjection.getUnmatchedTransactions(tenantId);
    }

    @Override
    public List<TransactionView> getTransactionsWithDivergences(String tenantId) {
        log.debug("Getting transactions with divergences for tenant: {}", tenantId);
        return transactionProjection.getTransactionsWithDivergences(tenantId);
    }

    @Override
    public List<TransactionView> getTransactionsWithFraudSuspicion(String tenantId) {
        log.debug("Getting transactions with fraud suspicion for tenant: {}", tenantId);
        return transactionProjection.getTransactionsWithFraudSuspicion(tenantId);
    }

    @Override
    public EventStatistics getEventStatistics() {
        log.debug("Getting event statistics");

        long totalEvents = eventStore.getEventCount();
        // For now, we'll return a simple statistic
        // In a real implementation, we would calculate events by day/week
        long eventsByDay = totalEvents; // Placeholder
        long eventsByWeek = totalEvents; // Placeholder

        return new EventStatistics(totalEvents, eventsByDay, eventsByWeek);
    }
}