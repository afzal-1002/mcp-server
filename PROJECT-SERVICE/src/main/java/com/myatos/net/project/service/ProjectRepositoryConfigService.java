package com.myatos.net.project.service;

import com.myatos.net.project.dto.project.CreateProjectRepositoryRequest;
import com.myatos.net.project.dto.project.ProjectRepositoryResponse;
import com.myatos.net.project.dto.project.UpdateProjectRepositoryRequest;

import java.util.List;

public interface ProjectRepositoryConfigService {
    ProjectRepositoryResponse create(String projectKey, CreateProjectRepositoryRequest request);
    List<ProjectRepositoryResponse> listByProject(String projectKey);
    List<ProjectRepositoryResponse> listByProject(String projectKey, String baseUrl);
    ProjectRepositoryResponse getDefaultRepository(String projectKey);
    ProjectRepositoryResponse getDefaultRepository(String projectKey, String baseUrl);
    ProjectRepositoryResponse getRepository(String projectKey, Long repositoryId, String baseUrl);
    ProjectRepositoryResponse update(String projectKey, Long repositoryId, UpdateProjectRepositoryRequest request);
    ProjectRepositoryResponse update(String projectKey, Long repositoryId, String baseUrl, UpdateProjectRepositoryRequest request);
    void delete(String projectKey, Long repositoryId);
    void delete(String projectKey, Long repositoryId, String baseUrl);
}
