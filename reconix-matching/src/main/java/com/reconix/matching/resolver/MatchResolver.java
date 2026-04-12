package com.reconix.matching.resolver;
import com.reconix.matching.dto.MatchResult;
import java.util.List;

public interface MatchResolver {
    // O único método necessário é o que recebe os resultados potenciais e os resolve.
    List<MatchResult> resolve(List<MatchResult> potentialMatches);
}
