package com.reconix.ledger.service;

import com.reconix.ledger.dto.EventDto;
import com.reconix.ledger.projection.model.TransactionView;

import java.util.List;
import java.util.UUID;

public interface LedgerService {
    /**
     * Registers an event in the event store
     */
    void registerEvent(EventDto event);

    /**
     * Registers multiple events in the event store
     */
    void registerEvents(List<EventDto> events);

    /**
     * Gets all events for a specific transaction
     */
    List<EventDto> getTransactionEvents(String transactionId);

    /**
     * Gets all events for a specific tenant
     */
    List<EventDto> getTenantEvents(String tenantId);

    /**
     * Gets the current state of a transaction
     */
    TransactionView getTransactionState(String transactionId);

    /**
     * Gets all transactions for a tenant
     */
    List<TransactionView> getTenantTransactions(String tenantId);

    /**
     * Gets matched transactions for a tenant
     */
    List<TransactionView> getMatchedTransactions(String tenantId);

    /**
     * Gets unmatched transactions for a tenant
     */
    List<TransactionView> getUnmatchedTransactions(String tenantId);

    /**
     * Gets transactions with divergences for a tenant
     */
    List<TransactionView> getTransactionsWithDivergences(String tenantId);

    /**
     * Gets transactions with fraud suspicion for a tenant
     */
    List<TransactionView> getTransactionsWithFraudSuspicion(String tenantId);

    /**
     * Gets event statistics
     */
    EventStatistics getEventStatistics();
}

record EventStatistics(long totalEvents, long eventsByDay, long eventsByWeek) {}