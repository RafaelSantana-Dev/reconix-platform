package com.reconix.matching.strategy.impl;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.strategy.MatchingStrategy;
import java.time.temporal.ChronoUnit;

public class DateRangeMatchStrategy implements MatchingStrategy {
    private final double weight;
    private final int maxDaysDifference;

    public DateRangeMatchStrategy(double weight, int maxDaysDifference) {
        this.weight = weight;
        this.maxDaysDifference = maxDaysDifference;
    }

    @Override
    public double calculateSimilarity(Transaction txA, Transaction txB) {
        long daysBetween = Math.abs(ChronoUnit.DAYS.between(txA.transactionDate(), txB.transactionDate()));
        if (daysBetween <= maxDaysDifference) {
            return 1.0 - (double) daysBetween / (maxDaysDifference + 1);
        }
        return 0.0;
    }

    @Override
    public String getName() { return "DateRangeMatch"; }

    @Override
    public double getWeight() { return this.weight; }
}
