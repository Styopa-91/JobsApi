package com.project.jobsapi.service;

import com.project.jobsapi.model.JobDto;
import lombok.Data;

import java.util.List;

@Data
public class JobResponse {
    private List<JobDto> data;
}
