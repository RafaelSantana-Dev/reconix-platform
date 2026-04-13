package com.reconix.matching.engine;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.dto.MatchResult;
import com.reconix.matching.engine.impl.DefaultMatchingEngine;
import com.reconix.matching.strategy.impl.ExactMatchStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DefaultMatchingEngineTest {

    private DefaultMatchingEngine matchingEngine;

    @BeforeEach
    void setUp() {
        matchingEngine = new DefaultMatchingEngine();
        // Adiciona uma estratégia simples para o teste
        matchingEngine.addStrategy(new ExactMatchStrategy(1.0));
    }

    @Test
    void whenTransactionsMatchExactly_shouldReturnScoreOfOne() {
        Transaction txA = new Transaction(UUID.randomUUID(), "t-1", "bank", "Test", new BigDecimal("100.00"), LocalDate.now(), "BRL", null, null);
        Transaction txB = new Transaction(UUID.randomUUID(), "t-1", "erp", "Test", new BigDecimal("100.00"), LocalDate.now(), "BRL", null, null);

        MatchResult result = matchingEngine.calculateDetailedMatch(txA, txB);

        assertEquals(1.0, result.similarityScore(), 0.01);
    }

    @Test
    void whenTransactionsDoNotMatch_shouldReturnScoreOfZero() {
        Transaction txA = new Transaction(UUID.randomUUID(), "t-1", "bank", "Test A", new BigDecimal("100.00"), LocalDate.now(), "BRL", null, null);
        Transaction txB = new Transaction(UUID.randomUUID(), "t-1", "erp", "Test B", new BigDecimal("200.00"), LocalDate.now().plusDays(1), "BRL", null, null);

        MatchResult result = matchingEngine.calculateDetailedMatch(txA, txB);

        assertEquals(0.0, result.similarityScore(), 0.01);
    }
}
