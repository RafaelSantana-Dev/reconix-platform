package com.reconix.fraud.producer;

import com.reconix.fraud.config.RabbitMQConfig;
import com.reconix.fraud.dto.FraudAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishFraudAlert(FraudAlertEvent event) {
        log.info("Publishing fraud alert: {}", event.getAlertId());
        
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.FRAUD_ALERT_EXCHANGE,
                RabbitMQConfig.FRAUD_ALERT_ROUTING_KEY,
                event
            );
            log.info("Fraud alert published successfully: {}", event.getAlertId());
        } catch (Exception e) {
            log.error("Error publishing fraud alert: {}", event.getAlertId(), e);
            throw e;
        }
    }
}
