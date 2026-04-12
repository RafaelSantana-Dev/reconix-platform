package com.reconix.matching.resolver.impl;

import com.reconix.matching.dto.MatchResult;
import com.reconix.matching.dto.MatchStatus;
import com.reconix.matching.resolver.MatchResolver;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DefaultMatchResolver implements MatchResolver {
    @Override
    public List<MatchResult> resolve(List<MatchResult> potentialMatches) {
        Map<UUID, List<MatchResult>> groupedByTxA = potentialMatches.stream()
            .collect(Collectors.groupingBy(MatchResult::transactionAId));
            
        List<MatchResult> finalMatches = new ArrayList<>();
        Set<UUID> matchedTxB = new HashSet<>();

        // Prioritize a melhor correspondência para cada transação A
        for (Map.Entry<UUID, List<MatchResult>> entry : groupedByTxA.entrySet()) {
            List<MatchResult> candidates = entry.getValue().stream()
                .filter(r -> !matchedTxB.contains(r.transactionBId()))
                .sorted(Comparator.comparingDouble(MatchResult::similarityScore).reversed())
                .toList();

            if (candidates.isEmpty()) continue;

            MatchResult bestMatch = candidates.get(0);
            
            // Lógica de conflito simples: se o segundo melhor candidato também for bom, marque como conflito
            if (candidates.size() > 1 && candidates.get(1).similarityScore() > 0.6) {
                finalMatches.add(new MatchResult(bestMatch.matchId(), bestMatch.transactionAId(), bestMatch.transactionBId(), 
                    bestMatch.tenantId(), bestMatch.similarityScore(), MatchStatus.CONFLICT, bestMatch.matchDetails()));
            } else {
                finalMatches.add(bestMatch);
                matchedTxB.add(bestMatch.transactionBId());
            }
        }
        return finalMatches;
    }
}
