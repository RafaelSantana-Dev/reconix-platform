package com.reconix.ingestion.dto;

import java.time.Instant;
import java.util.UUID;

public record FileIngestedEvent(
        UUID eventId,
        UUID fileId,
        String tenantId,
        String originalFileName,
        String storagePath,
        long fileSize,
        String contentType,
        Instant ingestedAt
) {}
