package com.reconix.fraud.repository;

import com.reconix.fraud.domain.AlertStatus;
import com.reconix.fraud.domain.FraudAlert;
import com.reconix.fraud.domain.RiskLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FraudAlertRepository extends MongoRepository<FraudAlert, String> {
    
    List<FraudAlert> findByTenantId(String tenantId);
    
    List<FraudAlert> findByTenantIdAndStatus(String tenantId, AlertStatus status);
    
    List<FraudAlert> findByTenantIdAndRiskLevel(String tenantId, RiskLevel riskLevel);
    
    List<FraudAlert> findByTenantIdAndDetectedAtBetween(
        String tenantId, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
    
    Long countByTenantIdAndStatus(String tenantId, AlertStatus status);
    
    boolean existsByTransactionId(String transactionId);
}
