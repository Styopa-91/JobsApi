package com.project.jobsapi;

import com.project.jobsapi.controller.JobController;
import com.project.jobsapi.model.Job;
import com.project.jobsapi.model.LocationStatistics;
import com.project.jobsapi.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
public class JobControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllJobs_ShouldReturnJobsPage() throws Exception {
        // Arrange
        Job job = new Job();
        job.setId(1L); // Set the ID to a non-null value
        job.setTitle("Sample Job"); // Set other fields as necessary

        when(jobService.getAllJobs(0, 10, "createdAt")).thenReturn(new PageImpl<>(Arrays.asList(job)));

        // Act & Assert
        mockMvc.perform(get("/api/jobs")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    void getTopJobs_ShouldReturnTop10Jobs() throws Exception {
        // Arrange
        Job job = new Job();
        job.setId(1L); // Set the ID to a non-null value
        job.setTitle("Sample Job"); // Set other fields as necessary

        when(jobService.getTopJobs()).thenReturn(Arrays.asList(job));

        // Act & Assert
        mockMvc.perform(get("/api/jobs/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void getLocationStatistics_ShouldReturnStatistics() throws Exception {
        // Arrange
        LocationStatistics locationStatistics = new LocationStatistics("Test Location", 10L);
        when(jobService.getLocationStatistics()).thenReturn(List.of(locationStatistics));

        // Act & Assert
        mockMvc.perform(get("/api/jobs/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].location").value("Test Location"));
    }

}
