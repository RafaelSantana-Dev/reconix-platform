package com.reconix.matching.engine;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.dto.MatchResult;
import com.reconix.matching.strategy.MatchingStrategy;

import java.util.List;

public interface MatchingEngine {
    /**
     * Calculates the overall similarity score between two transactions using registered strategies
     */
    double calculateOverallSimilarity(Transaction transactionA, Transaction transactionB);

    /**
     * Gets detailed breakdown of how the similarity was calculated
     */
    MatchResult calculateDetailedMatch(Transaction transactionA, Transaction transactionB);

    /**
     * Adds a matching strategy to the engine
     */
    void addStrategy(MatchingStrategy strategy);

    /**
     * Gets all registered strategies
     */
    List<MatchingStrategy> getStrategies();
}