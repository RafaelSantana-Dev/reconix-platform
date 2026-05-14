package com.reconix.reporting.dto;

import com.reconix.reporting.domain.ReportFormat;
import com.reconix.reporting.domain.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    
    private String tenantId;
    private ReportType reportType;
    private ReportFormat format;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
