package com.reconix.matching.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Currency;

public record Amount(BigDecimal value, Currency currency) {

    public Amount {
        if (value == null) throw new IllegalArgumentException("Value cannot be null");
        if (currency == null) throw new IllegalArgumentException("Currency cannot be null");
        value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public Amount add(Amount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add amounts with different currencies");
        }
        return new Amount(this.value.add(other.value), this.currency);
    }

    public Amount subtract(Amount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract amounts with different currencies");
        }
        return new Amount(this.value.subtract(other.value), this.currency);
    }

    public boolean isCloseTo(Amount other, BigDecimal tolerance) {
        if (!this.currency.equals(other.currency)) {
            return false;
        }
        BigDecimal difference = this.value.subtract(other.value).abs();
        return difference.compareTo(tolerance) <= 0;
    }

    public double similarityWith(Amount other, BigDecimal tolerance) {
        if (!this.currency.equals(other.currency)) {
            return 0.0;
        }

        if (this.isCloseTo(other, tolerance)) {
            // Higher tolerance means less precision required, so higher similarity
            BigDecimal difference = this.value.subtract(other.value).abs();
            if (difference.compareTo(BigDecimal.ZERO) == 0) {
                return 1.0;
            }

            // Calculate similarity based on tolerance
            BigDecimal similarity = BigDecimal.ONE.subtract(difference.divide(tolerance, 4, BigDecimal.ROUND_HALF_UP));
            return Math.max(0.0, similarity.doubleValue());
        }

        return 0.0;
    }
}