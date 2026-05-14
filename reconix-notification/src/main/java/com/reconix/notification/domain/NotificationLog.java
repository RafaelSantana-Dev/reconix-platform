package com.reconix.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notification_logs")
public class NotificationLog {

    @Id
    private String id;
    
    private String tenantId;
    private String alertId;
    
    private NotificationType type;
    private NotificationStatus status;
    
    private String recipient;
    private String subject;
    private String message;
    
    private Map<String, Object> metadata;
    
    private String errorMessage;
    private Integer retryCount;
    
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
