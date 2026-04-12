package com.reconix.matching.strategy.impl;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.strategy.MatchingStrategy;
import java.math.BigDecimal;

public class AmountToleranceStrategy implements MatchingStrategy {
    private final double weight;
    private final BigDecimal tolerance;

    public AmountToleranceStrategy(double weight, BigDecimal tolerance) {
        this.weight = weight;
        this.tolerance = tolerance;
    }

    @Override
    public double calculateSimilarity(Transaction txA, Transaction txB) {
        BigDecimal difference = txA.amount().subtract(txB.amount()).abs();
        if (difference.compareTo(tolerance) <= 0) {
            return 1.0 - (difference.doubleValue() / tolerance.doubleValue());
        }
        return 0.0;
    }

    @Override
    public String getName() { return "AmountToleranceMatch"; }

    @Override
    public double getWeight() { return this.weight; }
}
