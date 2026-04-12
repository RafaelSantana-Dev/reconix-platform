package com.reconix.matching.consumer;

import com.reconix.matching.config.RabbitMQConfig;
import com.reconix.matching.dto.FileProcessedEvent;
import com.reconix.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final MatchingService matchingService;

    @RabbitListener(queues = RabbitMQConfig.FILE_PROCESSED_QUEUE)
    public void receiveFileProcessedEvent(FileProcessedEvent event) {
        try {
            matchingService.processFileEvent(event);
        } catch (Exception e) {
            log.error("Error processing event {}: {}", event.eventId(), e.getMessage(), e);
            // Em produção, reenviar para uma DLQ (Dead Letter Queue)
        }
    }
}
