package com.reconix.matching.engine.impl;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.dto.MatchDetail;
import com.reconix.matching.dto.MatchResult;
import com.reconix.matching.dto.MatchStatus;
import com.reconix.matching.engine.MatchingEngine;
import com.reconix.matching.strategy.MatchingStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultMatchingEngine implements MatchingEngine {
    private final List<MatchingStrategy> strategies = new ArrayList<>();

    @Override
    public MatchResult calculateDetailedMatch(Transaction txA, Transaction txB) {
        List<MatchDetail> details = new ArrayList<>();
        double totalScore = 0.0;
        double totalWeight = 0.0;

        for (MatchingStrategy strategy : strategies) {
            double score = strategy.calculateSimilarity(txA, txB);
            details.add(new MatchDetail(strategy.getName(), score, null));
            totalScore += score * strategy.getWeight();
            totalWeight += strategy.getWeight();
        }

        double finalScore = totalWeight > 0 ? totalScore / totalWeight : 0.0;
        return new MatchResult(UUID.randomUUID(), txA.id(), txB.id(), txA.tenantId(),
                finalScore, determineStatus(finalScore), details);
    }
    
    @Override
    public void addStrategy(MatchingStrategy strategy) { this.strategies.add(strategy); }

    // MÉTODO FALTANTE ADICIONADO AQUI
    @Override
    public List<MatchingStrategy> getStrategies() {
        return List.copyOf(this.strategies);
    }

    private MatchStatus determineStatus(double score) {
        if (score >= 0.85) return MatchStatus.MATCHED;
        if (score >= 0.50) return MatchStatus.PARTIAL_MATCH;
        return MatchStatus.UNMATCHED;
    }
}
