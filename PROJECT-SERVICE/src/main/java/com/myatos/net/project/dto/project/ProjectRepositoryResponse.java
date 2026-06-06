package com.myatos.net.project.dto.project;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ProjectRepositoryResponse {
    private Long id;
    private String baseUrl;
    private String projectKey;
    private String repoName;
    private String repoUrl;
    private String defaultBranch;
    private Long credentialId;
    private boolean primaryRepository;
    private boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
