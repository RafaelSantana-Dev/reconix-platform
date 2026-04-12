package com.reconix.matching.dto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
public record MatchFoundEvent(
    UUID eventId, 
    UUID matchId, 
    String tenantId, 
    UUID transactionAId, 
    UUID transactionBId,
    double similarityScore, 
    MatchStatus status, 
    List<MatchDetail> matchDetails, 
    Instant matchedAt
) {}
