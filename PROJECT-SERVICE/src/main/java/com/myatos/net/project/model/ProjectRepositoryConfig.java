package com.myatos.net.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "project_repository_configs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_project_repo_url", columnNames = {"jira_base_url", "project_key", "repo_url"}),
                @UniqueConstraint(name = "uk_project_repo_name", columnNames = {"jira_base_url", "project_key", "repo_name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRepositoryConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_repository_config_id")
    private Long id;

    @Column(name = "jira_base_url", nullable = false, length = 512)
    private String baseUrl;

    @Column(name = "project_key", nullable = false, length = 32)
    private String projectKey;

    @Column(name = "repo_name", nullable = false, length = 255)
    private String repoName;

    @Column(name = "repo_url", nullable = false, length = 1024)
    private String repoUrl;

    @Column(name = "default_branch", nullable = false, length = 255)
    private String defaultBranch;

    @Column(name = "credential_id", nullable = false)
    private Long credentialId;

    @Column(name = "is_primary", nullable = false)
    private boolean primaryRepository;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
