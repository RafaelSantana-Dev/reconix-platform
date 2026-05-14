package com.reconix.ledger.projection;

import com.reconix.ledger.dto.*;
import com.reconix.ledger.projection.model.TransactionView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionProjection implements Projection<Map<String, TransactionView>> {

    private final Map<String, TransactionView> transactionViews;

    public TransactionProjection() {
        this.transactionViews = new ConcurrentHashMap<>();
    }

    @Override
    public void handle(EventDto event) {
        switch (event) {
            case TransactionCreatedEvent e -> handleTransactionCreated(e);
            case TransactionMatchedEvent e -> handleTransactionMatched(e);
            case TransactionUnmatchedEvent e -> handleTransactionUnmatched(e);
            case DivergenceDetectedEvent e -> handleDivergenceDetected(e);
            case FraudSuspectedEvent e -> handleFraudSuspected(e);
            default -> throw new IllegalArgumentException("Unknown event type: " + event.getClass().getSimpleName());
        }
    }

    private void handleTransactionCreated(TransactionCreatedEvent event) {
        TransactionView view = TransactionView.builder()
            .transactionId(event.transactionId().toString())
            .tenantId(event.tenantId())
            .source(event.source())
            .description(event.description())
            .amount(event.amount())
            .transactionDate(event.transactionDate())
            .currency(event.currency())
            .referenceNumber(event.referenceNumber())
            .category(event.category())
            .status("CREATED")
            .createdAt(event.occurredAt())
            .updatedAt(event.occurredAt())
            .build();

        transactionViews.put(event.transactionId().toString(), view);
    }

    private void handleTransactionMatched(TransactionMatchedEvent event) {
        TransactionView existing = transactionViews.get(event.transactionAId().toString());
        if (existing != null) {
            TransactionView updated = existing.toBuilder()
                .status("MATCHED")
                .matchInfo("Matched with " + event.transactionBId() + ", Score: " + event.similarityScore())
                .matchStatus(event.matchStatus())
                .updatedAt(event.occurredAt())
                .build();

            transactionViews.put(event.transactionAId().toString(), updated);
        }
    }

    private void handleTransactionUnmatched(TransactionUnmatchedEvent event) {
        TransactionView existing = transactionViews.get(event.transactionId().toString());
        if (existing != null) {
            TransactionView updated = existing.toBuilder()
                .status("UNMATCHED")
                .reason(event.reason())
                .updatedAt(event.occurredAt())
                .build();

            transactionViews.put(event.transactionId().toString(), updated);
        }
    }

    private void handleDivergenceDetected(DivergenceDetectedEvent event) {
        TransactionView existing = transactionViews.get(event.transactionAId().toString());
        if (existing != null) {
            TransactionView updated = existing.toBuilder()
                .status("DIVERGENCE_DETECTED")
                .divergenceInfo(event.divergenceType() + ": " + event.amountDifference())
                .updatedAt(event.occurredAt())
                .build();

            transactionViews.put(event.transactionAId().toString(), updated);
        }
    }

    private void handleFraudSuspected(FraudSuspectedEvent event) {
        TransactionView existing = transactionViews.get(event.transactionId().toString());
        if (existing != null) {
            TransactionView updated = existing.toBuilder()
                .status("FRAUD_SUSPECTED")
                .fraudInfo(event.fraudType() + " - Risk: " + event.riskLevel())
                .updatedAt(event.occurredAt())
                .build();

            transactionViews.put(event.transactionId().toString(), updated);
        }
    }

    @Override
    public Map<String, TransactionView> getCurrentState() {
        return new ConcurrentHashMap<>(transactionViews);
    }

    @Override
    public void reset() {
        transactionViews.clear();
    }

    public List<TransactionView> getTransactionsByTenant(String tenantId) {
        return transactionViews.values().stream()
            .filter(view -> tenantId.equals(view.tenantId()))
            .collect(Collectors.toList());
    }

    public TransactionView getTransactionById(String transactionId) {
        return transactionViews.get(transactionId);
    }

    public List<TransactionView> getMatchedTransactions(String tenantId) {
        return transactionViews.values().stream()
            .filter(view -> tenantId.equals(view.tenantId()))
            .filter(view -> "MATCHED".equals(view.status()))
            .collect(Collectors.toList());
    }

    public List<TransactionView> getUnmatchedTransactions(String tenantId) {
        return transactionViews.values().stream()
            .filter(view -> tenantId.equals(view.tenantId()))
            .filter(view -> "UNMATCHED".equals(view.status()))
            .collect(Collectors.toList());
    }

    public List<TransactionView> getTransactionsWithDivergences(String tenantId) {
        return transactionViews.values().stream()
            .filter(view -> tenantId.equals(view.tenantId()))
            .filter(view -> "DIVERGENCE_DETECTED".equals(view.status()))
            .collect(Collectors.toList());
    }

    public List<TransactionView> getTransactionsWithFraudSuspicion(String tenantId) {
        return transactionViews.values().stream()
            .filter(view -> tenantId.equals(view.tenantId()))
            .filter(view -> "FRAUD_SUSPECTED".equals(view.status()))
            .collect(Collectors.toList());
    }
}