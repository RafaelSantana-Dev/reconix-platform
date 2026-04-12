package com.reconix.matching.strategy;

import com.reconix.matching.domain.Transaction;

public interface MatchingStrategy {
    double calculateSimilarity(Transaction transactionA, Transaction transactionB);
    String getName();
    double getWeight();
}