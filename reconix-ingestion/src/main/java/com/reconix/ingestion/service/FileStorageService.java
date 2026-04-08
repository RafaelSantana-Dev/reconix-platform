package com.reconix.ingestion.service;

import com.reconix.ingestion.config.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @PostConstruct
    private void ensureBucketExists() {
        try {
            String bucketName = minioProperties.bucketName();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                log.info("Bucket '{}' não encontrado. Criando...", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket '{}' criado com sucesso.", bucketName);
            } else {
                log.info("Bucket '{}' já existe.", bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar o bucket do MinIO: " + e.getMessage(), e);
        }
    }

    public String uploadFile(MultipartFile file, String tenantId) {
        try {
            String fileName = file.getOriginalFilename();
            String objectName = String.format("%s/%s-%s", tenantId, UUID.randomUUID(), fileName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.bucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            log.info("Arquivo salvo no MinIO com sucesso: {}", objectName);
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo para o MinIO: " + e.getMessage(), e);
        }
    }
}
