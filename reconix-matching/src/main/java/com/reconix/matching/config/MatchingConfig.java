package com.reconix.matching.config;

import com.reconix.matching.engine.MatchingEngine;
import com.reconix.matching.engine.impl.DefaultMatchingEngine;
import com.reconix.matching.scorer.StringSimilarityScorer;
import com.reconix.matching.strategy.impl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
public class MatchingConfig {

    @Bean
    public MatchingEngine configuredMatchingEngine(
            StringSimilarityScorer scorer,
            @Value("${matching.exact.weight:0.4}") double exactWeight,
            @Value("${matching.fuzzy.weight:0.3}") double fuzzyWeight,
            @Value("${matching.daterange.weight:0.2}") double dateRangeWeight,
            @Value("${matching.amounttolerance.weight:0.1}") double amountToleranceWeight,
            @Value("${matching.amount.tolerance:0.50}") String amountTolerance,
            @Value("${matching.fuzzy.threshold:0.7}") double fuzzyThreshold,
            @Value("${matching.daterange.max-days:2}") int maxDays) {

        MatchingEngine engine = new DefaultMatchingEngine();

        engine.addStrategy(new ExactMatchStrategy(exactWeight));
        engine.addStrategy(new FuzzyMatchStrategy(fuzzyWeight, fuzzyThreshold, scorer));
        engine.addStrategy(new DateRangeMatchStrategy(dateRangeWeight, maxDays));
        engine.addStrategy(new AmountToleranceStrategy(amountToleranceWeight, new BigDecimal(amountTolerance)));
        
        return engine;
    }
}
