package com.reconix.scheduler.service;

import com.reconix.scheduler.dto.JobScheduleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final Scheduler scheduler;

    public List<Map<String, Object>> listAllJobs() throws SchedulerException {
        List<Map<String, Object>> jobList = new ArrayList<>();
        
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                
                Map<String, Object> jobInfo = new HashMap<>();
                jobInfo.put("jobName", jobKey.getName());
                jobInfo.put("jobGroup", jobKey.getGroup());
                jobInfo.put("description", jobDetail.getDescription());
                jobInfo.put("jobClass", jobDetail.getJobClass().getName());
                
                if (!triggers.isEmpty()) {
                    Trigger trigger = triggers.get(0);
                    jobInfo.put("nextFireTime", trigger.getNextFireTime());
                    jobInfo.put("previousFireTime", trigger.getPreviousFireTime());
                    jobInfo.put("state", scheduler.getTriggerState(trigger.getKey()).name());
                }
                
                jobList.add(jobInfo);
            }
        }
        
        return jobList;
    }

    public void scheduleJob(JobScheduleRequest request) throws SchedulerException {
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(request.getJobClass());
            
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(request.getJobName(), request.getJobGroup())
                .withDescription(request.getDescription())
                .storeDurably()
                .build();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(request.getJobName() + "-trigger", request.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(request.getCronExpression()))
                .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Job scheduled: {}", request.getJobName());
            
        } catch (ClassNotFoundException e) {
            log.error("Job class not found: {}", request.getJobClass(), e);
            throw new SchedulerException("Job class not found", e);
        }
    }

    public void pauseJob(String jobName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName);
        scheduler.pauseJob(jobKey);
        log.info("Job paused: {}", jobName);
    }

    public void resumeJob(String jobName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName);
        scheduler.resumeJob(jobKey);
        log.info("Job resumed: {}", jobName);
    }

    public void triggerJob(String jobName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName);
        scheduler.triggerJob(jobKey);
        log.info("Job triggered: {}", jobName);
    }

    public void deleteJob(String jobName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName);
        scheduler.deleteJob(jobKey);
        log.info("Job deleted: {}", jobName);
    }
}
