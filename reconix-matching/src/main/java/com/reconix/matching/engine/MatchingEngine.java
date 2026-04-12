package com.reconix.matching.engine;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.dto.MatchResult;
import com.reconix.matching.strategy.MatchingStrategy;
import java.util.List;

public interface MatchingEngine {
    // Este é o único método de cálculo que a implementação fornece
    MatchResult calculateDetailedMatch(Transaction transactionA, Transaction transactionB);

    void addStrategy(MatchingStrategy strategy);
    
    // Este método foi adicionado na correção anterior e deve permanecer
    List<MatchingStrategy> getStrategies();
}
