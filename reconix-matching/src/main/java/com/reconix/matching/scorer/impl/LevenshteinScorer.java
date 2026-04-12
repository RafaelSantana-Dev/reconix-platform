package com.reconix.matching.scorer.impl;

import com.reconix.matching.scorer.StringSimilarityScorer;
import org.springframework.stereotype.Component;

@Component
public class LevenshteinScorer implements StringSimilarityScorer {
    @Override
    public double calculate(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0.0;
        }
        String a = s1.toLowerCase();
        String b = s2.toLowerCase();
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        int distance = costs[b.length()];
        int maxLength = Math.max(a.length(), b.length());
        if (maxLength == 0) {
            return 1.0;
        }
        return 1.0 - ((double) distance / maxLength);
    }
}
