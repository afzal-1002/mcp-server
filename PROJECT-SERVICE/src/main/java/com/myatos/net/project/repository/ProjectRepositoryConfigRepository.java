package com.myatos.net.project.repository;

import com.myatos.net.project.model.ProjectRepositoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryConfigRepository extends JpaRepository<ProjectRepositoryConfig, Long> {
    List<ProjectRepositoryConfig> findAllByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseOrderByPrimaryRepositoryDescRepoNameAsc(String projectKey, String baseUrl);
    Optional<ProjectRepositoryConfig> findByIdAndProjectKeyIgnoreCaseAndBaseUrlIgnoreCase(Long repositoryId, String projectKey, String baseUrl);
    Optional<ProjectRepositoryConfig> findFirstByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndActiveTrueOrderByPrimaryRepositoryDescRepoNameAsc(String projectKey, String baseUrl);
    boolean existsByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndRepoUrlIgnoreCase(String projectKey, String baseUrl, String repoUrl);
    boolean existsByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndRepoUrlIgnoreCaseAndIdNot(String projectKey, String baseUrl, String repoUrl, Long repositoryId);
    boolean existsByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndRepoNameIgnoreCase(String projectKey, String baseUrl, String repoName);
    boolean existsByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndRepoNameIgnoreCaseAndIdNot(String projectKey, String baseUrl, String repoName, Long repositoryId);
}
