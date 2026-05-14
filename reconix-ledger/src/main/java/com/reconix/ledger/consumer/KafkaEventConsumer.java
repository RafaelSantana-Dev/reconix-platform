package com.reconix.ledger.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reconix.ledger.dto.TransactionMatchedEvent;
import com.reconix.ledger.service.LedgerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final LedgerService ledgerService;
    private final ObjectMapper objectMapper;

    // Listen to match found events from the matching service
    @KafkaListener(topics = "match.found", groupId = "ledger-service-group")
    public void receiveMatchFoundEvent(@Payload Map<String, Object> event) {
        log.info("Received match found event: {}", event.get("eventId"));

        try {
            // Extract data from the event map
            String eventIdStr = (String) event.get("eventId");
            String matchIdStr = (String) event.get("matchId");
            String tenantId = (String) event.get("tenantId");
            String transactionAIdStr = (String) event.get("transactionAId");
            String transactionBIdStr = (String) event.get("transactionBId");
            Double similarityScore = (Double) event.get("similarityScore");
            String status = (String) event.get("status");

            UUID eventId = eventIdStr != null ? UUID.fromString(eventIdStr) : UUID.randomUUID();
            UUID matchId = matchIdStr != null ? UUID.fromString(matchIdStr) : UUID.randomUUID();
            UUID transactionAId = transactionAIdStr != null ? UUID.fromString(transactionAIdStr) : null;
            UUID transactionBId = transactionBIdStr != null ? UUID.fromString(transactionBIdStr) : null;
            double score = similarityScore != null ? similarityScore : 0.0;
            String matchStatus = status != null ? status : "UNKNOWN";

            // Create our internal event
            TransactionMatchedEvent internalEvent = new TransactionMatchedEvent(
                transactionAId,
                transactionBId,
                tenantId,
                score,
                matchStatus
            );

            // Register the event in the ledger
            ledgerService.registerEvent(internalEvent);

            log.info("Match event processed successfully: {}", eventId);
        } catch (Exception e) {
            log.error("Error processing match found event: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Additional listeners can be added for other event types
    // For now, we'll focus on match events as they're the primary ones from matching service
}