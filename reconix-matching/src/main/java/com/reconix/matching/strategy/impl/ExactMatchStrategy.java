package com.reconix.matching.strategy.impl;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.strategy.MatchingStrategy;
import java.math.BigDecimal;

public class ExactMatchStrategy implements MatchingStrategy {
    private final double weight;
    
    public ExactMatchStrategy(double weight) {
        this.weight = weight;
    }

    @Override
    public double calculateSimilarity(Transaction txA, Transaction txB) {
        boolean amountMatch = txA.amount().compareTo(txB.amount()) == 0;
        boolean dateMatch = txA.transactionDate().isEqual(txB.transactionDate());
        return (amountMatch && dateMatch) ? 1.0 : 0.0;
    }

    @Override
    public String getName() { return "ExactMatch"; }

    @Override
    public double getWeight() { return this.weight; }
}
