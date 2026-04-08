package com.reconix.ingestion.producer;

import com.reconix.ingestion.dto.FileIngestedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final RabbitTemplate rabbitTemplate;

    // TODO: Mover exchange e routing key para o application.yml
    private static final String EXCHANGE_NAME = "reconix.events";
    private static final String ROUTING_KEY = "file.ingested";

    public void sendFileIngestedEvent(FileIngestedEvent event) {
        log.info("Publicando evento no RabbitMQ: {}", event.eventId());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event);
    }
}
