package edu.bsu.polaris.payload;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ProjectResponse {
    private Long id;
    private String project;
    private UserSummary createdBy;
    private Instant creationDateTime;
    private String fileName;
}
