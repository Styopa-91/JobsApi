package com.project.jobsapi.model;

import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.util.List;

@Data
public class JobDto {
    private Long id;
    private String slug;
    private String company_name;
    private String title;
    private String description;
    private boolean remote;
    private String url;
    @ElementCollection
    private List<String> tags;
    @ElementCollection
    private List<String> jobTypes;
    private String location;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Long created_at;
}
