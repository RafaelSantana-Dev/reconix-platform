package com.reconix.matching.dto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
public record FileProcessedEvent(
    UUID eventId, 
    String tenantId, 
    String fileId, 
    Instant processedAt, 
    List<TransactionDto> transactions
) {}
