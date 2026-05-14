package com.reconix.fraud.detector;

import com.reconix.fraud.dto.MatchFoundEvent;

public interface FraudRule {
    
    /**
     * Avalia se a transação é suspeita de fraude
     * @param event Evento de matching
     * @return Score de risco (0.0 a 1.0)
     */
    double evaluate(MatchFoundEvent event);
    
    /**
     * Nome da regra
     */
    String getRuleName();
    
    /**
     * Verifica se a regra está habilitada
     */
    boolean isEnabled();
}
