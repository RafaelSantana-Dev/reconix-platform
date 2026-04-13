package com.reconix.matching.service;

import com.reconix.matching.dto.*;
import com.reconix.matching.engine.MatchingEngine;
import com.reconix.matching.producer.KafkaProducer;
import com.reconix.matching.resolver.MatchResolver;
import com.reconix.matching.service.impl.DefaultMatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock
    private MatchingEngine matchingEngine;
    @Mock
    private MatchResolver matchResolver;
    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private DefaultMatchingService matchingService;

    private TransactionDto txBank;
    private TransactionDto txErp;

    @BeforeEach
    void setUp() {
        txBank = new TransactionDto(UUID.randomUUID(), "t-1", "bank", "Test", new BigDecimal("100.00"), LocalDate.now(), "BRL", null, null);
        txErp = new TransactionDto(UUID.randomUUID(), "t-1", "erp", "Test", new BigDecimal("100.00"), LocalDate.now(), "BRL", null, null);
    }

    @Test
    void givenFileEventWithTwoSources_shouldPerformMatchingAndPublish() {
        // Arrange
        FileProcessedEvent event = new FileProcessedEvent(UUID.randomUUID(), "t-1", "f-1", Instant.now(), List.of(txBank, txErp));
        MatchResult matchResult = new MatchResult(UUID.randomUUID(), txBank.id(), txErp.id(), "t-1", 1.0, MatchStatus.MATCHED, List.of());

        when(matchingEngine.calculateDetailedMatch(any(), any())).thenReturn(matchResult);
        when(matchResolver.resolve(anyList())).thenReturn(List.of(matchResult));
        doNothing().when(kafkaProducer).sendMatchFoundEvent(any(MatchFoundEvent.class));

        // Act
        List<MatchFoundEvent> resultEvents = matchingService.processFileEvent(event);

        // Assert
        assertNotNull(resultEvents);
        assertEquals(1, resultEvents.size());
        verify(kafkaProducer, times(1)).sendMatchFoundEvent(any(MatchFoundEvent.class));
    }
    
    @Test
    void givenFileEventWithOneSource_shouldDoNothing() {
        // Arrange
        FileProcessedEvent event = new FileProcessedEvent(UUID.randomUUID(), "t-1", "f-1", Instant.now(), List.of(txBank));
        
        // Act
        List<MatchFoundEvent> resultEvents = matchingService.processFileEvent(event);
        
        // Assert
        assertTrue(resultEvents.isEmpty());
        verifyNoInteractions(matchingEngine, matchResolver, kafkaProducer);
    }
}
