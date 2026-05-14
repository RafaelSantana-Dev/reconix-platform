package com.reconix.fraud.controller;

import com.reconix.fraud.domain.AlertStatus;
import com.reconix.fraud.domain.FraudAlert;
import com.reconix.fraud.domain.RiskLevel;
import com.reconix.fraud.repository.FraudAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
public class FraudController {

    private final FraudAlertRepository alertRepository;

    @GetMapping("/alerts")
    public ResponseEntity<List<FraudAlert>> getAlerts(
        @RequestParam String tenantId,
        @RequestParam(required = false) AlertStatus status,
        @RequestParam(required = false) RiskLevel riskLevel
    ) {
        log.info("Getting fraud alerts for tenant: {}", tenantId);

        List<FraudAlert> alerts;
        
        if (status != null) {
            alerts = alertRepository.findByTenantIdAndStatus(tenantId, status);
        } else if (riskLevel != null) {
            alerts = alertRepository.findByTenantIdAndRiskLevel(tenantId, riskLevel);
        } else {
            alerts = alertRepository.findByTenantId(tenantId);
        }

        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/alerts/{alertId}")
    public ResponseEntity<FraudAlert> getAlert(@PathVariable String alertId) {
        log.info("Getting fraud alert: {}", alertId);
        
        return alertRepository.findById(alertId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/alerts/period")
    public ResponseEntity<List<FraudAlert>> getAlertsByPeriod(
        @RequestParam String tenantId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        log.info("Getting fraud alerts for tenant: {} between {} and {}", 
            tenantId, startDate, endDate);

        List<FraudAlert> alerts = alertRepository.findByTenantIdAndDetectedAtBetween(
            tenantId, startDate, endDate
        );

        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/alerts/{alertId}/review")
    public ResponseEntity<FraudAlert> reviewAlert(
        @PathVariable String alertId,
        @RequestParam AlertStatus status,
        @RequestParam String reviewedBy,
        @RequestParam(required = false) String notes
    ) {
        log.info("Reviewing fraud alert: {} with status: {}", alertId, status);

        return alertRepository.findById(alertId)
            .map(alert -> {
                alert.setStatus(status);
                alert.setReviewedBy(reviewedBy);
                alert.setReviewedAt(LocalDateTime.now());
                alert.setReviewNotes(notes);
                alert.setUpdatedAt(LocalDateTime.now());
                
                FraudAlert updated = alertRepository.save(alert);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> getAlertCount(
        @RequestParam String tenantId,
        @RequestParam AlertStatus status
    ) {
        log.info("Getting alert count for tenant: {} with status: {}", tenantId, status);
        
        Long count = alertRepository.countByTenantIdAndStatus(tenantId, status);
        return ResponseEntity.ok(count);
    }
}
