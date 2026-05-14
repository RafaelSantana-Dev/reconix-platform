package com.reconix.notification.channel;

import com.reconix.notification.config.NotificationConfig;
import com.reconix.notification.dto.FraudAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookChannel implements NotificationChannel {

    private final WebClient.Builder webClientBuilder;
    private final NotificationConfig notificationConfig;

    @Override
    public void send(FraudAlertEvent event, String recipient) {
        if (!isEnabled()) {
            log.debug("Webhook channel is disabled");
            return;
        }

        if (recipient == null || recipient.isBlank()) {
            log.warn("Webhook URL not provided");
            return;
        }

        try {
            int timeout = notificationConfig.getWebhook().getTimeoutSeconds();

            webClientBuilder.build()
                .post()
                .uri(recipient)
                .bodyValue(event)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(timeout))
                .onErrorResume(e -> {
                    log.error("Error sending webhook notification to: {}", recipient, e);
                    return Mono.empty();
                })
                .block();

            log.info("Webhook notification sent successfully to: {}", recipient);
            
        } catch (Exception e) {
            log.error("Error sending webhook notification to: {}", recipient, e);
            throw new RuntimeException("Failed to send webhook notification", e);
        }
    }

    @Override
    public boolean isEnabled() {
        return notificationConfig.getWebhook().isEnabled();
    }

    @Override
    public String getChannelName() {
        return "WEBHOOK";
    }
}
