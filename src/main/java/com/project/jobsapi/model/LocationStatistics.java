package com.project.jobsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationStatistics {
    private String location;
    private long jobCount;
}
