package com.project.jobsapi.controller;

import com.project.jobsapi.model.Job;
import com.project.jobsapi.model.LocationStatistics;
import com.project.jobsapi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<Page<Job>> getAllJobs(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort) {
        return ResponseEntity.ok(jobService.getAllJobs(page, size, sort));
    }

    @GetMapping("/top")
    public ResponseEntity<List<Job>> getTopJobs() {
        return ResponseEntity.ok(jobService.getTopJobs());
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<LocationStatistics>> getLocationStatistics() {
        return ResponseEntity.ok(jobService.getLocationStatistics());
    }

    @GetMapping("/footer")
    public ResponseEntity<String> getString() {
        return ResponseEntity.ok("Hello world");
    }
}
