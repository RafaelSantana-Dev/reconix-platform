package com.reconix.matching.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public record MatchResult(
    UUID matchId, 
    UUID transactionAId, 
    UUID transactionBId, 
    String tenantId,
    double similarityScore,
    MatchStatus status, 
    List<MatchDetail> matchDetails
) {}
