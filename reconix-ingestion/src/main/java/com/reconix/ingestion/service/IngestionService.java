package com.reconix.ingestion.service;

import com.reconix.ingestion.dto.FileIngestedEvent;
import com.reconix.ingestion.dto.UploadResponse;
import com.reconix.ingestion.producer.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final FileStorageService fileStorageService;
    private final RabbitMQProducer rabbitMQProducer;

    public UploadResponse ingestFile(MultipartFile file, String tenantId) {
        // 1. Salvar o arquivo no MinIO e obter o caminho de armazenamento
        String storagePath = fileStorageService.uploadFile(file, tenantId);

        // 2. Gerar um ID único para este arquivo/evento
        UUID fileId = UUID.randomUUID();

        // 3. Criar o evento que será enviado para a fila
        FileIngestedEvent event = new FileIngestedEvent(
                UUID.randomUUID(),      // eventId
                fileId,                 // fileId
                tenantId,               // tenantId
                file.getOriginalFilename(),
                storagePath,
                file.getSize(),
                file.getContentType(),
                Instant.now()
        );

        // 4. Publicar o evento no RabbitMQ
        rabbitMQProducer.sendFileIngestedEvent(event);

        // 5. Retornar uma resposta de sucesso para o cliente
        return new UploadResponse(
                fileId,
                file.getOriginalFilename(),
                "PENDING",
                "Arquivo recebido e enfileirado para processamento."
        );
    }
}
