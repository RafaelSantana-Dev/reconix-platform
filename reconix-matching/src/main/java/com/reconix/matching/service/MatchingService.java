package com.reconix.matching.service;

import com.reconix.matching.dto.FileProcessedEvent;
import com.reconix.matching.dto.MatchFoundEvent;
import com.reconix.matching.dto.MatchResult;

import java.util.List;

public interface MatchingService {
    /**
     * Processes a file processing event and performs matching between transactions
     */
    List<MatchFoundEvent> processFileEvent(FileProcessedEvent event);

    /**
     * Performs matching between two lists of transactions
     */
    List<MatchResult> matchTransactions(List<com.reconix.matching.dto.TransactionDto> transactionsA,
                                       List<com.reconix.matching.dto.TransactionDto> transactionsB);

    /**
     * Performs matching for a specific tenant
     */
    List<MatchResult> matchForTenant(String tenantId, List<com.reconix.matching.dto.TransactionDto> transactions);
}