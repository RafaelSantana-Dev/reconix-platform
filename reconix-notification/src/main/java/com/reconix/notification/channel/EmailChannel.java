package com.reconix.notification.channel;

import com.reconix.notification.config.NotificationConfig;
import com.reconix.notification.dto.FraudAlertEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailChannel implements NotificationChannel {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final NotificationConfig notificationConfig;

    @Override
    public void send(FraudAlertEvent event, String recipient) {
        if (!isEnabled()) {
            log.debug("Email channel is disabled");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(
                notificationConfig.getEmail().getFrom(),
                notificationConfig.getEmail().getFromName()
            );
            helper.setTo(recipient);
            helper.setSubject(buildSubject(event));
            helper.setText(buildEmailBody(event), true);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", recipient);
            
        } catch (MessagingException e) {
            log.error("Error sending email to: {}", recipient, e);
            throw new RuntimeException("Failed to send email", e);
        } catch (Exception e) {
            log.error("Unexpected error sending email to: {}", recipient, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildSubject(FraudAlertEvent event) {
        return String.format("[%s] Alerta de Fraude Detectado - %s",
            event.getRiskLevel(),
            event.getTransactionId()
        );
    }

    private String buildEmailBody(FraudAlertEvent event) {
        Context context = new Context();
        context.setVariable("alertId", event.getAlertId());
        context.setVariable("riskLevel", event.getRiskLevel());
        context.setVariable("riskScore", String.format("%.2f", event.getRiskScore() * 100));
        context.setVariable("transactionId", event.getTransactionId());
        context.setVariable("transactionDescription", event.getTransactionDescription());
        context.setVariable("transactionAmount", event.getTransactionAmount());
        context.setVariable("transactionDate", 
            event.getTransactionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );
        context.setVariable("triggeredRules", event.getTriggeredRules());
        context.setVariable("detectionReason", event.getDetectionReason());
        context.setVariable("detectedAt", 
            event.getDetectedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );

        return templateEngine.process("fraud-alert-email", context);
    }

    @Override
    public boolean isEnabled() {
        return notificationConfig.getEmail().isEnabled();
    }

    @Override
    public String getChannelName() {
        return "EMAIL";
    }
}
