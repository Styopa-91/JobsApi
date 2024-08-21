package com.project.jobsapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String slug;
    private String companyName;
    private String title;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private boolean remote;
    private String url;
    @ElementCollection
    private List<String> tags;
    @ElementCollection
    private List<String> jobTypes;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
