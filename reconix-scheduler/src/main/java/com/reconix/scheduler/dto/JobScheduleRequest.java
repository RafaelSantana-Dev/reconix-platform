package com.reconix.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobScheduleRequest {
    
    private String jobName;
    private String jobGroup;
    private String cronExpression;
    private String description;
    private String jobClass;
}
