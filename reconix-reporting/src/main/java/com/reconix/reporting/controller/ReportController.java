package com.reconix.reporting.controller;

import com.reconix.reporting.dto.ReportRequest;
import com.reconix.reporting.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate/pdf")
    public ResponseEntity<byte[]> generatePdfReport(@RequestBody ReportRequest request) {
        log.info("Generating PDF report for tenant: {}", request.getTenantId());
        
        byte[] pdfBytes = reportService.generatePdfReport(request);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report.pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes);
    }

    @PostMapping("/generate/excel")
    public ResponseEntity<byte[]> generateExcelReport(@RequestBody ReportRequest request) {
        log.info("Generating Excel report for tenant: {}", request.getTenantId());
        
        byte[] excelBytes = reportService.generateExcelReport(request);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "report.xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(excelBytes);
    }
}
