package com.reconix.matching.producer;

import com.reconix.matching.dto.MatchFoundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String MATCH_FOUND_TOPIC = "t.match.found";

    public void sendMatchFoundEvent(MatchFoundEvent event) {
        try {
            kafkaTemplate.send(MATCH_FOUND_TOPIC, event.eventId().toString(), event);
            log.debug("Published match found event {} to Kafka", event.eventId());
        } catch (Exception e) {
            log.error("Failed to publish match found event {}: {}", event.eventId(), e.getMessage(), e);
        }
    }
}
