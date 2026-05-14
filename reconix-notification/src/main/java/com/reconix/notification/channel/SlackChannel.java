package com.reconix.notification.channel;

import com.reconix.notification.config.NotificationConfig;
import com.reconix.notification.dto.FraudAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackChannel implements NotificationChannel {

    private final WebClient.Builder webClientBuilder;
    private final NotificationConfig notificationConfig;

    @Override
    public void send(FraudAlertEvent event, String recipient) {
        if (!isEnabled()) {
            log.debug("Slack channel is disabled");
            return;
        }

        String webhookUrl = notificationConfig.getSlack().getWebhookUrl();
        
        if (webhookUrl == null || webhookUrl.isBlank()) {
            log.warn("Slack webhook URL not configured");
            return;
        }

        try {
            Map<String, Object> payload = buildSlackPayload(event);

            webClientBuilder.build()
                .post()
                .uri(webhookUrl)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    log.error("Error sending Slack notification", e);
                    return Mono.empty();
                })
                .block();

            log.info("Slack notification sent successfully");
            
        } catch (Exception e) {
            log.error("Error sending Slack notification", e);
            throw new RuntimeException("Failed to send Slack notification", e);
        }
    }

    private Map<String, Object> buildSlackPayload(FraudAlertEvent event) {
        Map<String, Object> payload = new HashMap<>();
        
        String color = switch (event.getRiskLevel()) {
            case "CRITICAL" -> "danger";
            case "HIGH" -> "warning";
            case "MEDIUM" -> "#FFA500";
            default -> "good";
        };

        String text = String.format(
            "*🚨 Alerta de Fraude Detectado*\n\n" +
            "*Nível de Risco:* %s (%.0f%%)\n" +
            "*Transação:* %s\n" +
            "*Valor:* R$ %s\n" +
            "*Descrição:* %s\n" +
            "*Data:* %s\n" +
            "*Regras Acionadas:* %s\n" +
            "*Motivo:* %s",
            event.getRiskLevel(),
            event.getRiskScore() * 100,
            event.getTransactionId(),
            event.getTransactionAmount(),
            event.getTransactionDescription(),
            event.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            String.join(", ", event.getTriggeredRules()),
            event.getDetectionReason()
        );

        Map<String, Object> attachment = new HashMap<>();
        attachment.put("color", color);
        attachment.put("text", text);
        attachment.put("footer", "Reconix Fraud Detection");
        attachment.put("ts", System.currentTimeMillis() / 1000);

        payload.put("attachments", new Object[]{attachment});

        return payload;
    }

    @Override
    public boolean isEnabled() {
        return notificationConfig.getSlack().isEnabled();
    }

    @Override
    public String getChannelName() {
        return "SLACK";
    }
}
