package com.reconix.ingestion.controller;

import com.reconix.ingestion.dto.UploadResponse;
import com.reconix.ingestion.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ingestion")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService ingestionService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-Tenant-ID") String tenantId) {

        // TODO: Adicionar validação do tenantId e do arquivo (tamanho/tipo)

        UploadResponse response = ingestionService.ingestFile(file, tenantId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
