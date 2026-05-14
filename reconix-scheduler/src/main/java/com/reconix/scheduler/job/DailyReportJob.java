package com.reconix.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class DailyReportJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Executing Daily Report Job at: {}", LocalDateTime.now());
        
        try {
            // TODO: Chamar o serviço de reporting via Feign
            // reportingClient.generateDailyReport();
            
            log.info("Daily Report Job completed successfully");
            
        } catch (Exception e) {
            log.error("Error executing Daily Report Job", e);
            throw new JobExecutionException(e);
        }
    }
}
