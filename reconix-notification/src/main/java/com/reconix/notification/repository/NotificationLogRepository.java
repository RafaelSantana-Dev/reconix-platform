package com.reconix.notification.repository;

import com.reconix.notification.domain.NotificationLog;
import com.reconix.notification.domain.NotificationStatus;
import com.reconix.notification.domain.NotificationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationLogRepository extends MongoRepository<NotificationLog, String> {
    
    List<NotificationLog> findByTenantId(String tenantId);
    
    List<NotificationLog> findByTenantIdAndStatus(String tenantId, NotificationStatus status);
    
    List<NotificationLog> findByTenantIdAndType(String tenantId, NotificationType type);
    
    List<NotificationLog> findByAlertId(String alertId);
    
    List<NotificationLog> findByStatusAndRetryCountLessThan(NotificationStatus status, Integer maxRetries);
    
    Long countByTenantIdAndStatus(String tenantId, NotificationStatus status);
    
    List<NotificationLog> findByTenantIdAndCreatedAtBetween(
        String tenantId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
}
