package com.reconix.reporting.controller;

import com.reconix.reporting.dto.DashboardKPIs;
import com.reconix.reporting.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpis")
    public ResponseEntity<DashboardKPIs> getKPIs(
        @RequestParam String tenantId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        log.info("Getting KPIs for tenant: {} between {} and {}", tenantId, startDate, endDate);
        
        DashboardKPIs kpis = dashboardService.calculateKPIs(tenantId, startDate, endDate);
        return ResponseEntity.ok(kpis);
    }
}
