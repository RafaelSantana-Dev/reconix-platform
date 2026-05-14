package com.reconix.scheduler.controller;

import com.reconix.scheduler.dto.JobScheduleRequest;
import com.reconix.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService schedulerService;

    @GetMapping("/jobs")
    public ResponseEntity<List<Map<String, Object>>> listJobs() throws SchedulerException {
        log.info("Listing all scheduled jobs");
        List<Map<String, Object>> jobs = schedulerService.listAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/jobs/schedule")
    public ResponseEntity<String> scheduleJob(@RequestBody JobScheduleRequest request) throws SchedulerException {
        log.info("Scheduling new job: {}", request.getJobName());
        schedulerService.scheduleJob(request);
        return ResponseEntity.ok("Job scheduled successfully");
    }

    @PutMapping("/jobs/{jobName}/pause")
    public ResponseEntity<String> pauseJob(@PathVariable String jobName) throws SchedulerException {
        log.info("Pausing job: {}", jobName);
        schedulerService.pauseJob(jobName);
        return ResponseEntity.ok("Job paused successfully");
    }

    @PutMapping("/jobs/{jobName}/resume")
    public ResponseEntity<String> resumeJob(@PathVariable String jobName) throws SchedulerException {
        log.info("Resuming job: {}", jobName);
        schedulerService.resumeJob(jobName);
        return ResponseEntity.ok("Job resumed successfully");
    }

    @PostMapping("/jobs/{jobName}/trigger")
    public ResponseEntity<String> triggerJob(@PathVariable String jobName) throws SchedulerException {
        log.info("Triggering job immediately: {}", jobName);
        schedulerService.triggerJob(jobName);
        return ResponseEntity.ok("Job triggered successfully");
    }

    @DeleteMapping("/jobs/{jobName}")
    public ResponseEntity<String> deleteJob(@PathVariable String jobName) throws SchedulerException {
        log.info("Deleting job: {}", jobName);
        schedulerService.deleteJob(jobName);
        return ResponseEntity.ok("Job deleted successfully");
    }
}
