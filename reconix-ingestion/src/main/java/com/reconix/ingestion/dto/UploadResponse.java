package com.reconix.ingestion.dto;

import java.util.UUID;

public record UploadResponse(
        UUID fileId,
        String fileName,
        String status,
        String message
) {}
