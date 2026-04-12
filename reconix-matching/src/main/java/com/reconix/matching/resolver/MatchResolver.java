package com.reconix.matching.resolver;

import com.reconix.matching.dto.MatchResult;
import com.reconix.matching.dto.MatchStatus;
import com.reconix.matching.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface MatchResolver {
    MatchResult resolve(List<TransactionDto> transactionsA, List<TransactionDto> transactionsB);

    /**
     * Resolve potential conflicts when multiple transactions could match a single one
     */
    MatchResult resolveConflicts(List<MatchResult> potentialMatches);
}