package com.reconix.matching.service.impl;

import com.reconix.matching.domain.Transaction;
import com.reconix.matching.dto.*;
import com.reconix.matching.engine.MatchingEngine;
import com.reconix.matching.producer.KafkaProducer;
import com.reconix.matching.resolver.MatchResolver;
import com.reconix.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMatchingService implements MatchingService {
    private final MatchingEngine matchingEngine;
    private final MatchResolver matchResolver;
    private final KafkaProducer kafkaProducer;
    
    @Override
    public List<MatchFoundEvent> processFileEvent(FileProcessedEvent event) {
        log.info("Processing event {} for tenant {}", event.eventId(), event.tenantId());
        
        Map<String, List<TransactionDto>> txBySource = event.transactions().stream()
            .collect(Collectors.groupingBy(TransactionDto::source));
        
        List<String> sources = new ArrayList<>(txBySource.keySet());
        if (sources.size() < 2) {
            log.warn("Event {} has less than 2 sources, skipping matching.", event.eventId());
            return Collections.emptyList();
        }

        // Simplificação: assume as duas primeiras fontes para matching
        List<TransactionDto> transactionsA = txBySource.get(sources.get(0));
        List<TransactionDto> transactionsB = txBySource.get(sources.get(1));
        
        List<MatchResult> potentialMatches = new ArrayList<>();
        for (TransactionDto txA_dto : transactionsA) {
            for (TransactionDto txB_dto : transactionsB) {
                // Não fazer match de uma transação com ela mesma
                if (txA_dto.id().equals(txB_dto.id())) continue;

                Transaction txA = toDomain(txA_dto);
                Transaction txB = toDomain(txB_dto);
                potentialMatches.add(matchingEngine.calculateDetailedMatch(txA, txB));
            }
        }
        
        List<MatchResult> resolvedMatches = matchResolver.resolve(potentialMatches);
        
        List<MatchFoundEvent> foundEvents = resolvedMatches.stream()
            .map(this::toMatchFoundEvent)
            .collect(Collectors.toList());
            
        foundEvents.forEach(kafkaProducer::sendMatchFoundEvent);
        
        log.info("Finished processing event {}, found and published {} matches.", event.eventId(), foundEvents.size());
        return foundEvents;
    }

    private Transaction toDomain(TransactionDto dto) {
        return new Transaction(dto.id(), dto.tenantId(), dto.source(), dto.description(), dto.amount(),
                dto.transactionDate(), dto.currency(), dto.referenceNumber(), dto.category());
    }

    private MatchFoundEvent toMatchFoundEvent(MatchResult result) {
        return new MatchFoundEvent(UUID.randomUUID(), result.matchId(), result.tenantId(), result.transactionAId(),
                result.transactionBId(), result.similarityScore(), result.status(), result.matchDetails(), Instant.now());
    }
}
