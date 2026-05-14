package com.reconix.ledger.domain;

import com.reconix.ledger.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionAggregate extends AggregateRoot {
    private UUID transactionId;
    private String tenantId;
    private String source;
    private String description;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private String currency;
    private String referenceNumber;
    private String category;
    private TransactionStatus status;
    private List<UUID> matchedTransactions;
    private String matchReason;

    public TransactionAggregate(String aggregateId) {
        super(aggregateId);
        this.matchedTransactions = new ArrayList<>();
        this.status = TransactionStatus.PENDING;
    }

    public TransactionAggregate(UUID transactionId, String tenantId, String source, String description,
                              BigDecimal amount, LocalDate transactionDate, String currency,
                              String referenceNumber, String category) {
        super(transactionId.toString());
        this.transactionId = transactionId;
        this.tenantId = tenantId;
        this.source = source;
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.currency = currency;
        this.referenceNumber = referenceNumber;
        this.category = category;
        this.matchedTransactions = new ArrayList<>();
        this.status = TransactionStatus.CREATED;
    }

    @Override
    public void apply(EventDto event) {
        switch (event) {
            case TransactionCreatedEvent e -> apply(e);
            case TransactionMatchedEvent e -> apply(e);
            case TransactionUnmatchedEvent e -> apply(e);
            case DivergenceDetectedEvent e -> apply(e);
            case FraudSuspectedEvent e -> apply(e);
            default -> throw new IllegalArgumentException("Unknown event type: " + event.getClass().getSimpleName());
        }
    }

    private void apply(TransactionCreatedEvent event) {
        this.transactionId = event.transactionId();
        this.tenantId = event.tenantId();
        this.source = event.source();
        this.description = event.description();
        this.amount = event.amount();
        this.transactionDate = event.transactionDate();
        this.currency = event.currency();
        this.referenceNumber = event.referenceNumber();
        this.category = event.category();
        this.status = TransactionStatus.CREATED;
        incrementVersion();
    }

    private void apply(TransactionMatchedEvent event) {
        this.matchedTransactions.add(event.transactionBId());
        this.status = TransactionStatus.MATCHED;
        this.matchReason = "Matched with transaction " + event.transactionBId() +
                          " (score: " + event.similarityScore() + ")";
        incrementVersion();
    }

    private void apply(TransactionUnmatchedEvent event) {
        this.status = TransactionStatus.UNMATCHED;
        this.matchReason = event.reason();
        incrementVersion();
    }

    private void apply(DivergenceDetectedEvent event) {
        this.status = TransactionStatus.DIVERGENCE_DETECTED;
        this.matchReason = "Divergence detected: " + event.divergenceType() +
                          " (diff: " + event.amountDifference() + ")";
        incrementVersion();
    }

    private void apply(FraudSuspectedEvent event) {
        this.status = TransactionStatus.FRAUD_SUSPECTED;
        incrementVersion();
    }

    // Getters
    public UUID getTransactionId() { return transactionId; }
    public String getTenantId() { return tenantId; }
    public String getSource() { return source; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public String getCurrency() { return currency; }
    public String getReferenceNumber() { return referenceNumber; }
    public String getCategory() { return category; }
    public TransactionStatus getStatus() { return status; }
    public List<UUID> getMatchedTransactions() { return new ArrayList<>(matchedTransactions); }
    public String getMatchReason() { return matchReason; }
}

enum TransactionStatus {
    PENDING,
    CREATED,
    MATCHED,
    UNMATCHED,
    DIVERGENCE_DETECTED,
    FRAUD_SUSPECTED
}