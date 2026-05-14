package com.reconix.notification.service;

import com.reconix.notification.channel.NotificationChannel;
import com.reconix.notification.domain.NotificationLog;
import com.reconix.notification.domain.NotificationStatus;
import com.reconix.notification.domain.NotificationType;
import com.reconix.notification.dto.FraudAlertEvent;
import com.reconix.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationChannel> channels;
    private final NotificationLogRepository logRepository;

    @Async
    public void sendNotifications(FraudAlertEvent event, List<String> recipients) {
        log.info("Sending notifications for alert: {}", event.getAlertId());

        for (NotificationChannel channel : channels) {
            if (channel.isEnabled()) {
                for (String recipient : recipients) {
                    sendNotification(channel, event, recipient);
                }
            }
        }
    }

    private void sendNotification(NotificationChannel channel, FraudAlertEvent event, String recipient) {
        NotificationLog log = createLog(channel, event, recipient);
        
        try {
            channel.send(event, recipient);
            
            log.setStatus(NotificationStatus.SENT);
            log.setSentAt(LocalDateTime.now());
            
        } catch (Exception e) {
            this.log.error("Error sending notification via {}: {}", channel.getChannelName(), e.getMessage());
            
            log.setStatus(NotificationStatus.FAILED);
            log.setErrorMessage(e.getMessage());
            log.setRetryCount(log.getRetryCount() + 1);
        }

        log.setUpdatedAt(LocalDateTime.now());
        logRepository.save(log);
    }

    private NotificationLog createLog(NotificationChannel channel, FraudAlertEvent event, String recipient) {
        LocalDateTime now = LocalDateTime.now();
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("riskLevel", event.getRiskLevel());
        metadata.put("riskScore", event.getRiskScore());
        metadata.put("triggeredRules", event.getTriggeredRules());

        return NotificationLog.builder()
            .tenantId(event.getTenantId())
            .alertId(event.getAlertId())
            .type(NotificationType.valueOf(channel.getChannelName()))
            .status(NotificationStatus.PENDING)
            .recipient(recipient)
            .subject(buildSubject(event))
            .message(buildMessage(event))
            .metadata(metadata)
            .retryCount(0)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private String buildSubject(FraudAlertEvent event) {
        return String.format("Alerta de Fraude - %s", event.getTransactionId());
    }

    private String buildMessage(FraudAlertEvent event) {
        return String.format(
            "Alerta de fraude detectado para a transação %s com nível de risco %s",
            event.getTransactionId(),
            event.getRiskLevel()
        );
    }
}
