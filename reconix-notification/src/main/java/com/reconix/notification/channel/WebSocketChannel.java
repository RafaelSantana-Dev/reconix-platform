package com.reconix.notification.channel;

import com.reconix.notification.config.NotificationConfig;
import com.reconix.notification.dto.FraudAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannel implements NotificationChannel {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationConfig notificationConfig;

    @Override
    public void send(FraudAlertEvent event, String recipient) {
        if (!isEnabled()) {
            log.debug("WebSocket channel is disabled");
            return;
        }

        try {
            // Envia para tópico específico do tenant
            String destination = String.format("/topic/fraud-alerts/%s", event.getTenantId());
            messagingTemplate.convertAndSend(destination, event);
            
            log.info("WebSocket notification sent to: {}", destination);
            
        } catch (Exception e) {
            log.error("Error sending WebSocket notification", e);
            throw new RuntimeException("Failed to send WebSocket notification", e);
        }
    }

    @Override
    public boolean isEnabled() {
        return notificationConfig.getWebsocket().isEnabled();
    }

    @Override
    public String getChannelName() {
        return "WEBSOCKET";
    }
}
