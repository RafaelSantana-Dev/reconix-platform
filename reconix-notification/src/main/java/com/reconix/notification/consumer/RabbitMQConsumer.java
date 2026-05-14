package com.reconix.notification.consumer;

import com.reconix.notification.dto.FraudAlertEvent;
import com.reconix.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "fraud.alert.queue")
    public void consumeFraudAlert(FraudAlertEvent event) {
        log.info("Received fraud alert: {}", event.getAlertId());
        
        try {
            // TODO: Buscar destinatários configurados para o tenant
            // Por enquanto, usa um email padrão
            List<String> recipients = List.of("admin@reconix.com");
            
            notificationService.sendNotifications(event, recipients);
            
            log.info("Notifications sent for alert: {}", event.getAlertId());
        } catch (Exception e) {
            log.error("Error processing fraud alert: {}", event.getAlertId(), e);
            throw e; // Permite retry do RabbitMQ
        }
    }
}
