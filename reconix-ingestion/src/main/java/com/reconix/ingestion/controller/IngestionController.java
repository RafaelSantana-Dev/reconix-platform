package com.reconix.ingestion.controller;

import com.reconix.ingestion.dto.UploadResponse;
import com.reconix.ingestion.service.IngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/ingestion")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService ingestionService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestPart(value = "file", required = true) MultipartFile file,
            @RequestHeader(value = "X-Tenant-ID", required = false, defaultValue = "reconix-dev") String tenantId) {

        log.info("=== RECEBIDO UPLOAD ===");
        log.info("Tenant-ID: {}", tenantId);
        log.info("Arquivo: {}", file.getOriginalFilename());
        log.info("Tamanho: {} bytes", file.getSize());
        log.info("Content-Type: {}", file.getContentType());

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo vazio");
        }

        UploadResponse response = ingestionService.ingestFile(file, tenantId);
        return ResponseEntity.accepted().body(response);
    }
}
