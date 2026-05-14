package com.reconix.ledger.controller;

import com.reconix.ledger.dto.EventDto;
import com.reconix.ledger.projection.model.TransactionView;
import com.reconix.ledger.service.EventStatistics;
import com.reconix.ledger.service.LedgerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping("/events/{transactionId}")
    public ResponseEntity<List<EventDto>> getTransactionEvents(@PathVariable String transactionId) {
        log.info("Getting events for transaction: {}", transactionId);
        List<EventDto> events = ledgerService.getTransactionEvents(transactionId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/tenant/{tenantId}/events")
    public ResponseEntity<List<EventDto>> getTenantEvents(@PathVariable String tenantId) {
        log.info("Getting events for tenant: {}", tenantId);
        List<EventDto> events = ledgerService.getTenantEvents(tenantId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<TransactionView> getTransactionState(@PathVariable String transactionId) {
        log.info("Getting state for transaction: {}", transactionId);
        TransactionView transaction = ledgerService.getTransactionState(transactionId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tenant/{tenantId}/transactions")
    public ResponseEntity<List<TransactionView>> getTenantTransactions(@PathVariable String tenantId) {
        log.info("Getting all transactions for tenant: {}", tenantId);
        List<TransactionView> transactions = ledgerService.getTenantTransactions(tenantId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/tenant/{tenantId}/matched")
    public ResponseEntity<List<TransactionView>> getMatchedTransactions(@PathVariable String tenantId) {
        log.info("Getting matched transactions for tenant: {}", tenantId);
        List<TransactionView> transactions = ledgerService.getMatchedTransactions(tenantId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/tenant/{tenantId}/unmatched")
    public ResponseEntity<List<TransactionView>> getUnmatchedTransactions(@PathVariable String tenantId) {
        log.info("Getting unmatched transactions for tenant: {}", tenantId);
        List<TransactionView> transactions = ledgerService.getUnmatchedTransactions(tenantId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/tenant/{tenantId}/divergences")
    public ResponseEntity<List<TransactionView>> getTransactionsWithDivergences(@PathVariable String tenantId) {
        log.info("Getting transactions with divergences for tenant: {}", tenantId);
        List<TransactionView> transactions = ledgerService.getTransactionsWithDivergences(tenantId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/tenant/{tenantId}/frauds")
    public ResponseEntity<List<TransactionView>> getTransactionsWithFraudSuspicion(@PathVariable String tenantId) {
        log.info("Getting transactions with fraud suspicion for tenant: {}", tenantId);
        List<TransactionView> transactions = ledgerService.getTransactionsWithFraudSuspicion(tenantId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/statistics")
    public ResponseEntity<EventStatistics> getEventStatistics() {
        log.info("Getting event statistics");
        EventStatistics statistics = ledgerService.getEventStatistics();
        return ResponseEntity.ok(statistics);
    }
}