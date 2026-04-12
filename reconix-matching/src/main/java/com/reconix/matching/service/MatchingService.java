package com.reconix.matching.service;
import com.reconix.matching.dto.FileProcessedEvent;
import com.reconix.matching.dto.MatchFoundEvent;
import java.util.List;

public interface MatchingService {
    // O único método necessário é o que processa o evento de entrada.
    List<MatchFoundEvent> processFileEvent(FileProcessedEvent event);
}
