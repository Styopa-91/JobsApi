package com.project.jobsapi;

import com.project.jobsapi.model.Job;
import com.project.jobsapi.model.JobDto;
import com.project.jobsapi.model.LocationStatistics;
import com.project.jobsapi.repository.JobRepository;
import com.project.jobsapi.service.JobResponse;
import com.project.jobsapi.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class JobServiceTest {
    @Mock
    private JobRepository jobRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JobService jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchJobs_ShouldSaveJobs_WhenResponseIsNotEmpty() {
        // Arrange
        JobDto jobDto = new JobDto();
        jobDto.setId(1L);
        jobDto.setSlug("test-slug");
        jobDto.setCompany_name("Test Company");
        jobDto.setTitle("Test Job");
        jobDto.setDescription("Test Description");
        jobDto.setRemote(true);
        jobDto.setUrl("http://test.com");
        jobDto.setTags(Arrays.asList("Java", "Spring"));
        jobDto.setJobTypes(Arrays.asList("Full-time"));
        jobDto.setLocation("Test Location");
        jobDto.setCreated_at(Instant.now().getEpochSecond());

        JobResponse jobResponse = new JobResponse();
        jobResponse.setData(Arrays.asList(jobDto));

        when(restTemplate.getForObject(anyString(), eq(JobResponse.class)))
                .thenReturn(jobResponse);

        // Act
        jobService.fetchJobs();

        // Assert
        verify(jobRepository, times(5)).saveAll(anyList());
    }

    @Test
    void getAllJobs_ShouldReturnPageOfJobs() {
        // Arrange
        Job job = new Job();
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(job));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        when(jobRepository.findAll(pageable)).thenReturn(jobPage);

        // Act
        Page<Job> result = jobService.getAllJobs(0, 10, "createdAt");

        // Assert
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getTopJobs_ShouldReturnTop10Jobs() {
        // Arrange
        Job job = new Job();
        List<Job> jobs = Arrays.asList(job);
        Pageable pageable = PageRequest.of(0, 10);
        when(jobRepository.findTop10(pageable)).thenReturn(jobs);

        // Act
        List<Job> result = jobService.getTopJobs();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void getLocationStatistics_ShouldReturnLocationStatistics() {
        // Arrange
        LocationStatistics locationStatistics = new LocationStatistics("Test Location", 10L);
        List<LocationStatistics> statistics = Arrays.asList(locationStatistics);
        when(jobRepository.getLocationStatistics()).thenReturn(statistics);

        // Act
        List<LocationStatistics> result = jobService.getLocationStatistics();

        // Assert
        assertEquals(1, result.size());
    }
}
