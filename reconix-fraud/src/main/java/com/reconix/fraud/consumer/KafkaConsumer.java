package com.reconix.fraud.consumer;

import com.reconix.fraud.dto.MatchFoundEvent;
import com.reconix.fraud.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final FraudDetectionService fraudDetectionService;

    @KafkaListener(
        topics = "match.found",
        groupId = "fraud-detection-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMatchFoundEvent(MatchFoundEvent event) {
        log.info("Received match found event: {}", event.getMatchId());
        
        try {
            fraudDetectionService.analyzeTransaction(event);
            log.info("Fraud analysis completed for match: {}", event.getMatchId());
        } catch (Exception e) {
            log.error("Error analyzing transaction for fraud: {}", event.getMatchId(), e);
            throw e; // Permite retry do Kafka
        }
    }
}
