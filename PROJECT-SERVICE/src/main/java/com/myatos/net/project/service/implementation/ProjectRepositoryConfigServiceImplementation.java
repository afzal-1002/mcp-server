package com.myatos.net.project.service.implementation;

import com.myatos.net.project.client.CredentialClient;
import com.myatos.net.project.client.IntegrationCredentialClient;
import com.myatos.net.project.dto.credentials.IntegrationCredentialResponse;
import com.myatos.net.project.dto.credentials.UserCredentialResponse;
import com.myatos.net.project.dto.project.CreateProjectRepositoryRequest;
import com.myatos.net.project.dto.project.ProjectRepositoryResponse;
import com.myatos.net.project.dto.project.UpdateProjectRepositoryRequest;
import com.myatos.net.project.exceptions.ResourceNotFoundException;
import com.myatos.net.project.model.ProjectRepositoryConfig;
import com.myatos.net.project.repository.ProjectRepositoryConfigRepository;
import com.myatos.net.project.service.ProjectRepositoryConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProjectRepositoryConfigServiceImplementation implements ProjectRepositoryConfigService {

    private final ProjectRepositoryConfigRepository projectRepositoryConfigRepository;
    private final IntegrationCredentialClient integrationCredentialClient;
    private final CredentialClient credentialClient;

    @Override
    @Transactional
    public ProjectRepositoryResponse create(String projectKey, CreateProjectRepositoryRequest request) {
        validateCreate(request);
        String key = normalizeProjectKey(projectKey);
        String baseUrl = resolveSelectedJiraBaseUrl(request.getBaseUrl());
        validateCredential(request.getCredentialId());
        ensureUnique(key, baseUrl, request.getRepoName(), request.getRepoUrl(), null);

        ProjectRepositoryConfig config = ProjectRepositoryConfig.builder()
                .projectKey(key)
                .baseUrl(baseUrl)
                .repoName(normalizeRepoName(request.getRepoName()))
                .repoUrl(normalizeRepoUrl(request.getRepoUrl()))
                .defaultBranch(defaultBranch(request.getDefaultBranch()))
                .credentialId(request.getCredentialId())
                .primaryRepository(Boolean.TRUE.equals(request.getPrimaryRepository()))
                .active(!Boolean.FALSE.equals(request.getActive()))
                .build();

        if (config.isPrimaryRepository()) {
            clearPrimary(key, baseUrl);
        }

        return toResponse(projectRepositoryConfigRepository.save(config));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectRepositoryResponse> listByProject(String projectKey) {
        throw new IllegalArgumentException("baseUrl is required because Jira project keys can exist in multiple domains.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectRepositoryResponse> listByProject(String projectKey, String baseUrl) {
        String key = normalizeProjectKey(projectKey);
        String selectedBaseUrl = requireBaseUrl(baseUrl);
        return projectRepositoryConfigRepository
                .findAllByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseOrderByPrimaryRepositoryDescRepoNameAsc(key, selectedBaseUrl)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectRepositoryResponse getDefaultRepository(String projectKey) {
        throw new IllegalArgumentException("baseUrl is required because Jira project keys can exist in multiple domains.");
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectRepositoryResponse getDefaultRepository(String projectKey, String baseUrl) {
        String key = normalizeProjectKey(projectKey);
        String selectedBaseUrl = requireBaseUrl(baseUrl);
        ProjectRepositoryConfig config = projectRepositoryConfigRepository
                .findFirstByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndActiveTrueOrderByPrimaryRepositoryDescRepoNameAsc(key, selectedBaseUrl)
                .orElseThrow(() -> new ResourceNotFoundException("No active repository configured for project: " + key + " at baseUrl: " + selectedBaseUrl));
        return toResponse(config);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectRepositoryResponse getRepository(String projectKey, Long repositoryId, String baseUrl) {
        if (repositoryId == null) {
            throw new IllegalArgumentException("repositoryId is required");
        }
        String key = normalizeProjectKey(projectKey);
        String selectedBaseUrl = requireBaseUrl(baseUrl);
        ProjectRepositoryConfig config = projectRepositoryConfigRepository
                .findByIdAndProjectKeyIgnoreCaseAndBaseUrlIgnoreCase(repositoryId, key, selectedBaseUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Repository config not found: " + repositoryId));
        return toResponse(config);
    }

    @Override
    @Transactional
    public ProjectRepositoryResponse update(String projectKey, Long repositoryId, UpdateProjectRepositoryRequest request) {
        throw new IllegalArgumentException("baseUrl is required because Jira project keys can exist in multiple domains.");
    }

    @Override
    @Transactional
    public ProjectRepositoryResponse update(String projectKey, Long repositoryId, String baseUrl, UpdateProjectRepositoryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required");
        }

        String key = normalizeProjectKey(projectKey);
        String selectedBaseUrl = requireBaseUrl(baseUrl);
        ProjectRepositoryConfig config = projectRepositoryConfigRepository
                .findByIdAndProjectKeyIgnoreCaseAndBaseUrlIgnoreCase(repositoryId, key, selectedBaseUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Repository config not found: " + repositoryId));

        String repoName = request.getRepoName() == null ? config.getRepoName() : normalizeRepoName(request.getRepoName());
        String repoUrl = request.getRepoUrl() == null ? config.getRepoUrl() : normalizeRepoUrl(request.getRepoUrl());
        ensureUnique(key, selectedBaseUrl, repoName, repoUrl, repositoryId);

        config.setRepoName(repoName);
        config.setRepoUrl(repoUrl);
        if (request.getDefaultBranch() != null) {
            config.setDefaultBranch(defaultBranch(request.getDefaultBranch()));
        }
        if (request.getCredentialId() != null) {
            validateCredential(request.getCredentialId());
            config.setCredentialId(request.getCredentialId());
        }
        if (request.getPrimaryRepository() != null) {
            if (request.getPrimaryRepository()) {
                clearPrimary(key, selectedBaseUrl);
            }
            config.setPrimaryRepository(request.getPrimaryRepository());
        }
        if (request.getActive() != null) {
            config.setActive(request.getActive());
        }

        return toResponse(projectRepositoryConfigRepository.save(config));
    }

    @Override
    @Transactional
    public void delete(String projectKey, Long repositoryId) {
        throw new IllegalArgumentException("baseUrl is required because Jira project keys can exist in multiple domains.");
    }

    @Override
    @Transactional
    public void delete(String projectKey, Long repositoryId, String baseUrl) {
        String key = normalizeProjectKey(projectKey);
        String selectedBaseUrl = requireBaseUrl(baseUrl);
        ProjectRepositoryConfig config = projectRepositoryConfigRepository
                .findByIdAndProjectKeyIgnoreCaseAndBaseUrlIgnoreCase(repositoryId, key, selectedBaseUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Repository config not found: " + repositoryId));
        projectRepositoryConfigRepository.delete(config);
    }

    private void validateCreate(CreateProjectRepositoryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required");
        }
        if (trimToNull(request.getBaseUrl()) == null) {
            throw new IllegalArgumentException("baseUrl is required");
        }
        if (trimToNull(request.getRepoName()) == null) {
            throw new IllegalArgumentException("repoName is required");
        }
        if (trimToNull(request.getRepoUrl()) == null) {
            throw new IllegalArgumentException("repoUrl is required");
        }
        if (request.getCredentialId() == null) {
            throw new IllegalArgumentException("credentialId is required");
        }
    }

    private void ensureUnique(String projectKey, String baseUrl, String repoName, String repoUrl, Long repositoryId) {
        boolean repoNameExists = repositoryId == null
                ? projectRepositoryConfigRepository.existsByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndRepoNameIgnoreCase(projectKey, baseUrl, repoName)
                : projectRepositoryConfigRepository.existsByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndRepoNameIgnoreCaseAndIdNot(projectKey, baseUrl, repoName, repositoryId);
        if (repoNameExists) {
            throw new IllegalArgumentException("Repository name already linked to project: " + repoName);
        }

        boolean repoUrlExists = repositoryId == null
                ? projectRepositoryConfigRepository.existsByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndRepoUrlIgnoreCase(projectKey, baseUrl, repoUrl)
                : projectRepositoryConfigRepository.existsByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseAndRepoUrlIgnoreCaseAndIdNot(projectKey, baseUrl, repoUrl, repositoryId);
        if (repoUrlExists) {
            throw new IllegalArgumentException("Repository URL already linked to project: " + repoUrl);
        }
    }

    private void validateCredential(Long credentialId) {
        IntegrationCredentialResponse credential = integrationCredentialClient.getById(credentialId);
        if (credential == null) {
            throw new IllegalArgumentException("Credential not found: " + credentialId);
        }
    }

    private String resolveSelectedJiraBaseUrl(String baseUrl) {
        String selectedBaseUrl = requireBaseUrl(baseUrl);
        UserCredentialResponse credential = credentialClient.getResolvedCredentialForCurrentUser(null, selectedBaseUrl);
        if (credential == null || trimToNull(credential.getBaseUrl()) == null) {
            throw new IllegalStateException("No Jira domain credential found for selected baseUrl: " + selectedBaseUrl);
        }
        return normalizeBaseUrl(credential.getBaseUrl());
    }

    private void clearPrimary(String projectKey, String baseUrl) {
        List<ProjectRepositoryConfig> configs = projectRepositoryConfigRepository
                .findAllByProjectKeyIgnoreCaseAndBaseUrlIgnoreCaseOrderByPrimaryRepositoryDescRepoNameAsc(projectKey, baseUrl);
        for (ProjectRepositoryConfig config : configs) {
            if (config.isPrimaryRepository()) {
                config.setPrimaryRepository(false);
            }
        }
    }

    private ProjectRepositoryResponse toResponse(ProjectRepositoryConfig config) {
        return ProjectRepositoryResponse.builder()
                .id(config.getId())
                .baseUrl(config.getBaseUrl())
                .projectKey(config.getProjectKey())
                .repoName(config.getRepoName())
                .repoUrl(config.getRepoUrl())
                .defaultBranch(config.getDefaultBranch())
                .credentialId(config.getCredentialId())
                .primaryRepository(config.isPrimaryRepository())
                .active(config.isActive())
                .createdAt(config.getCreatedAt())
                .updatedAt(config.getUpdatedAt())
                .build();
    }

    private String normalizeProjectKey(String projectKey) {
        String key = trimToNull(projectKey);
        if (key == null) {
            throw new IllegalArgumentException("projectKey is required");
        }
        return key.toUpperCase(Locale.ROOT);
    }

    private String requireBaseUrl(String baseUrl) {
        return normalizeBaseUrl(baseUrl);
    }

    private String normalizeRepoName(String repoName) {
        String trimmed = trimToNull(repoName);
        if (trimmed == null) {
            throw new IllegalArgumentException("repoName is required");
        }
        return trimmed;
    }

    private String normalizeRepoUrl(String repoUrl) {
        String trimmed = trimToNull(repoUrl);
        if (trimmed == null) {
            throw new IllegalArgumentException("repoUrl is required");
        }
        return trimmed;
    }

    private String defaultBranch(String branch) {
        String trimmed = trimToNull(branch);
        return trimmed == null ? "main" : trimmed;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeBaseUrl(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            throw new IllegalArgumentException("baseUrl is required");
        }
        if (!normalized.startsWith("http://") && !normalized.startsWith("https://")) {
            normalized = "https://" + normalized;
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
