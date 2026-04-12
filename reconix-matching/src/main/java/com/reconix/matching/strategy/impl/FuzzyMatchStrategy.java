package com.reconix.matching.strategy.impl;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.scorer.StringSimilarityScorer;
import com.reconix.matching.strategy.MatchingStrategy;

public class FuzzyMatchStrategy implements MatchingStrategy {
    private final double weight;
    private final double threshold;
    private final StringSimilarityScorer scorer;

    public FuzzyMatchStrategy(double weight, double threshold, StringSimilarityScorer scorer) {
        this.weight = weight;
        this.threshold = threshold;
        this.scorer = scorer;
    }

    @Override
    public double calculateSimilarity(Transaction txA, Transaction txB) {
        double score = scorer.calculate(txA.description(), txB.description());
        return score >= threshold ? score : 0.0;
    }

    @Override
    public String getName() { return "FuzzyMatch"; }

    @Override
    public double getWeight() { return this.weight; }
}
