package com.reconix.reporting.generator;

import com.reconix.reporting.dto.ReportRequest;

public interface ReportGenerator {
    
    /**
     * Gera o relatório no formato específico
     */
    byte[] generate(ReportRequest request);
    
    /**
     * Retorna o formato do relatório
     */
    String getFormat();
}
