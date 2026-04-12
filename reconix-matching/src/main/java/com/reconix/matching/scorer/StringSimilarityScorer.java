package com.reconix.matching.scorer;

public interface StringSimilarityScorer {
    double calculate(String str1, String str2);
}