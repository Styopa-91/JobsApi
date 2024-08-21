package com.project.jobsapi;

import com.project.jobsapi.model.Job;
import com.project.jobsapi.model.LocationStatistics;
import com.project.jobsapi.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class JobRepositoryTest {
    @Autowired
    private JobRepository jobRepository;

    @Test
    void findTop10_ShouldReturnTop10Jobs() {
        // Arrange
        for (int i = 1; i <= 15; i++) {
            Job job = new Job();
            job.setCreatedAt(LocalDateTime.now());
            jobRepository.save(job);
        }

        // Act
        List<Job> jobs = jobRepository.findTop10(PageRequest.of(0, 10));

        // Assert
        assertThat(jobs.size()).isEqualTo(10);
    }

    @Test
    void getLocationStatistics_ShouldReturnStatistics() {
        // Arrange
        Job job1 = new Job();
        job1.setLocation("Location1");
        jobRepository.save(job1);

        Job job2 = new Job();
        job2.setLocation("Location1");
        jobRepository.save(job2);

        Job job3 = new Job();
        job3.setLocation("Location2");
        jobRepository.save(job3);

        // Act
        List<LocationStatistics> statistics = jobRepository.getLocationStatistics();

        // Assert
        assertThat(statistics).hasSize(2);
        assertThat(statistics).extracting(LocationStatistics::getLocation).containsExactlyInAnyOrder("Location1", "Location2");
    }
}
