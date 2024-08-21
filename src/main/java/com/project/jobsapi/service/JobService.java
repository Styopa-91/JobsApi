package com.project.jobsapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.jobsapi.model.Job;
import com.project.jobsapi.model.JobDto;
import com.project.jobsapi.model.LocationStatistics;
import com.project.jobsapi.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    private final String apiUrl = "https://www.arbeitnow.com/api/job-board-api?page=";

    @Scheduled(fixedRate = 3600000) // 1 hour interval
    public void fetchJobs() {
        for (int i = 1; i <= 5; i++) {
            JobResponse response = restTemplate.getForObject(apiUrl + i, JobResponse.class);
            if (response != null && response.getData() != null) {
                List<Job> jobs = response.getData().stream().map(this::convertToJob).collect(Collectors.toList());
                jobRepository.saveAll(jobs);
            }
        }
    }

    private Job convertToJob(JobDto jobDto) {
        Job job = new Job();

        // Convert timestamp to LocalDateTime
        Long timestamp = jobDto.getCreated_at();
        LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
        job.setCreatedAt(createdAt);

        // Set other fields from JobDto to Job
        job.setId(jobDto.getId());
        job.setSlug(jobDto.getSlug());
        job.setCompanyName(jobDto.getCompany_name());
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setRemote(jobDto.isRemote());
        job.setUrl(jobDto.getUrl());
        job.setTags(jobDto.getTags());
        job.setJobTypes(jobDto.getJobTypes());
        job.setLocation(jobDto.getLocation());
        return job;
    }

    public Page<Job> getAllJobs(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return jobRepository.findAll(pageable);
    }

    public List<Job> getTopJobs() {
        Pageable pageable = PageRequest.of(0, 10);
        return jobRepository.findTop10(pageable);
    }

    public List<LocationStatistics> getLocationStatistics() {
        return jobRepository.getLocationStatistics();
    }
}
