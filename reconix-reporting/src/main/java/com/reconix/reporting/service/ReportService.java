package com.reconix.reporting.service;

import com.reconix.reporting.dto.ReportRequest;
import com.reconix.reporting.generator.ExcelReportGenerator;
import com.reconix.reporting.generator.PdfReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final PdfReportGenerator pdfGenerator;
    private final ExcelReportGenerator excelGenerator;

    public byte[] generatePdfReport(ReportRequest request) {
        log.info("Generating PDF report for tenant: {}", request.getTenantId());
        return pdfGenerator.generate(request);
    }

    public byte[] generateExcelReport(ReportRequest request) {
        log.info("Generating Excel report for tenant: {}", request.getTenantId());
        return excelGenerator.generate(request);
    }
}
