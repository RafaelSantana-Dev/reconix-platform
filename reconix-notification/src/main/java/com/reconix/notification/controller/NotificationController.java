package com.reconix.notification.controller;

import com.reconix.notification.domain.NotificationLog;
import com.reconix.notification.domain.NotificationStatus;
import com.reconix.notification.domain.NotificationType;
import com.reconix.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationLogRepository logRepository;

    @GetMapping("/logs")
    public ResponseEntity<List<NotificationLog>> getLogs(
        @RequestParam String tenantId,
        @RequestParam(required = false) NotificationStatus status,
        @RequestParam(required = false) NotificationType type
    ) {
        log.info("Getting notification logs for tenant: {}", tenantId);

        List<NotificationLog> logs;
        
        if (status != null) {
            logs = logRepository.findByTenantIdAndStatus(tenantId, status);
        } else if (type != null) {
            logs = logRepository.findByTenantIdAndType(tenantId, type);
        } else {
            logs = logRepository.findByTenantId(tenantId);
        }

        return ResponseEntity.ok(logs);
    }

    @GetMapping("/logs/{logId}")
    public ResponseEntity<NotificationLog> getLog(@PathVariable String logId) {
        log.info("Getting notification log: {}", logId);
        
        return logRepository.findById(logId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/logs/alert/{alertId}")
    public ResponseEntity<List<NotificationLog>> getLogsByAlert(@PathVariable String alertId) {
        log.info("Getting notification logs for alert: {}", alertId);
        
        List<NotificationLog> logs = logRepository.findByAlertId(alertId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/logs/period")
    public ResponseEntity<List<NotificationLog>> getLogsByPeriod(
        @RequestParam String tenantId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        log.info("Getting notification logs for tenant: {} between {} and {}", 
            tenantId, startDate, endDate);

        List<NotificationLog> logs = logRepository.findByTenantIdAndCreatedAtBetween(
            tenantId, startDate, endDate
        );

        return ResponseEntity.ok(logs);
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> getNotificationCount(
        @RequestParam String tenantId,
        @RequestParam NotificationStatus status
    ) {
        log.info("Getting notification count for tenant: {} with status: {}", tenantId, status);
        
        Long count = logRepository.countByTenantIdAndStatus(tenantId, status);
        return ResponseEntity.ok(count);
    }
}
