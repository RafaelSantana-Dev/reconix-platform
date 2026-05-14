package com.reconix.ledger.service;

import com.reconix.ledger.dto.TransactionCreatedEvent;
import com.reconix.ledger.dto.TransactionMatchedEvent;
import com.reconix.ledger.eventstore.EventStore;
import com.reconix.ledger.projection.TransactionProjection;
import com.reconix.ledger.projection.model.TransactionView;
import com.reconix.ledger.service.impl.DefaultLedgerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LedgerServiceTest {

    @Mock
    private EventStore eventStore;

    @Mock
    private TransactionProjection transactionProjection;

    private DefaultLedgerService ledgerService;

    @BeforeEach
    void setUp() {
        ledgerService = new DefaultLedgerService(eventStore, transactionProjection);
    }

    @Test
    void shouldRegisterTransactionCreatedEvent() {
        // Given
        UUID transactionId = UUID.randomUUID();
        TransactionCreatedEvent event = new TransactionCreatedEvent(
            transactionId,
            "tenant-1",
            "bank-statement",
            "Payment to Supplier ABC",
            new BigDecimal("1000.00"),
            LocalDate.now(),
            "BRL",
            "REF001",
            "payments"
        );

        // When
        ledgerService.registerEvent(event);

        // Then
        verify(eventStore, times(1)).saveEvent(event);
        verify(transactionProjection, times(1)).handle(event);
    }

    @Test
    void shouldRegisterTransactionMatchedEvent() {
        // Given
        UUID transactionAId = UUID.randomUUID();
        UUID transactionBId = UUID.randomUUID();
        TransactionMatchedEvent event = new TransactionMatchedEvent(
            transactionAId,
            transactionBId,
            "tenant-1",
            0.95,
            "MATCHED"
        );

        // When
        ledgerService.registerEvent(event);

        // Then
        verify(eventStore, times(1)).saveEvent(event);
        verify(transactionProjection, times(1)).handle(event);
    }

    @Test
    void shouldGetTransactionState() {
        // Given
        String transactionId = "some-transaction-id";
        TransactionView expectedView = TransactionView.builder()
            .transactionId(transactionId)
            .tenantId("tenant-1")
            .status("MATCHED")
            .build();

        when(transactionProjection.getTransactionById(transactionId)).thenReturn(expectedView);

        // When
        TransactionView result = ledgerService.getTransactionState(transactionId);

        // Then
        assertNotNull(result);
        assertEquals(transactionId, result.getTransactionId());
        assertEquals("tenant-1", result.getTenantId());
        assertEquals("MATCHED", result.getStatus());
    }

    @Test
    void shouldGetTenantTransactions() {
        // Given
        String tenantId = "tenant-1";
        List<TransactionView> expectedTransactions = List.of(
            TransactionView.builder().transactionId("1").tenantId(tenantId).build(),
            TransactionView.builder().transactionId("2").tenantId(tenantId).build()
        );

        when(transactionProjection.getTransactionsByTenant(tenantId)).thenReturn(expectedTransactions);

        // When
        List<TransactionView> result = ledgerService.getTenantTransactions(tenantId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(tenantId, result.get(0).getTenantId());
        assertEquals(tenantId, result.get(1).getTenantId());
    }
}