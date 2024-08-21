package com.project.jobsapi.repository;

import com.project.jobsapi.model.Job;
import com.project.jobsapi.model.LocationStatistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("SELECT j FROM Job j ORDER BY j.createdAt DESC")
    List<Job> findTop10(Pageable pageable);

    @Query("SELECT new com.project.jobsapi.model.LocationStatistics(j.location, COUNT(j)) " +
            "FROM Job j GROUP BY j.location ORDER BY COUNT(j) DESC")
    List<LocationStatistics> getLocationStatistics();
}
