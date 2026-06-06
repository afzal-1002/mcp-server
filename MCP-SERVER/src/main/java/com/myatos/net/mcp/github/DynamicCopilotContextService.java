package com.myatos.net.mcp.github;

import com.myatos.net.mcp.client.ProjectServiceClient;
import com.myatos.net.mcp.client.UserServiceClient;
import com.myatos.net.mcp.client.dto.ProjectRepositoryResponse;
import com.myatos.net.mcp.client.dto.ResolvedIntegrationCredentialResponse;
import com.myatos.net.mcp.client.dto.UserCredentialResponse;
import com.myatos.net.mcp.jira.JiraUrlFactory;
import org.springframework.stereotype.Service;

@Service
public class DynamicCopilotContextService {

    private final UserServiceClient userServiceClient;
    private final ProjectServiceClient projectServiceClient;
    private final JiraUrlFactory jiraUrlFactory;
    private final GitHubRepositoryUrlParser repositoryUrlParser;

    public DynamicCopilotContextService(
            UserServiceClient userServiceClient,
            ProjectServiceClient projectServiceClient,
            JiraUrlFactory jiraUrlFactory,
            GitHubRepositoryUrlParser repositoryUrlParser
    ) {
        this.userServiceClient = userServiceClient;
        this.projectServiceClient = projectServiceClient;
        this.jiraUrlFactory = jiraUrlFactory;
        this.repositoryUrlParser = repositoryUrlParser;
    }

    public DynamicCopilotContext resolve(String selectedJiraBaseUrl, String projectKey, Long repositoryId, String repositoryUrl) {
        String baseUrl = jiraUrlFactory.normalizeBaseUrl(requireNonBlank(selectedJiraBaseUrl, "jiraBaseUrl"));
        String resolvedProjectKey = requireNonBlank(projectKey, "projectKey").trim();

        UserCredentialResponse jiraCredential = userServiceClient.getResolvedJiraCredential(baseUrl);
        if (jiraCredential == null || isBlank(jiraCredential.username()) || isBlank(jiraCredential.token())) {
            throw new IllegalStateException("No resolved Jira credential found for selected baseUrl: " + baseUrl);
        }

        ProjectRepositoryResponse repository = resolveRepository(resolvedProjectKey, baseUrl, repositoryId, repositoryUrl);
        if (repository == null || isBlank(repository.repoUrl()) || repository.credentialId() == null) {
            throw new IllegalStateException("No default GitHub repository configured for project: " + resolvedProjectKey);
        }

        ResolvedIntegrationCredentialResponse githubCredential =
                userServiceClient.getResolvedIntegrationCredential(repository.credentialId());
        if (githubCredential == null || isBlank(githubCredential.secret())) {
            throw new IllegalStateException("No resolved GitHub credential found for repository credentialId: " + repository.credentialId());
        }

        GitHubRepositoryUrlParser.ParsedRepository parsedRepository = repositoryUrlParser.parse(repository.repoUrl());
        String branch = isBlank(repository.defaultBranch()) ? "main" : repository.defaultBranch().trim();

        return new DynamicCopilotContext(
                baseUrl,
                jiraCredential.username().trim(),
                jiraCredential.token().trim(),
                resolvedProjectKey,
                parsedRepository.owner(),
                parsedRepository.repository(),
                parsedRepository.fullName(),
                branch,
                githubCredential.secret().trim()
        );
    }

    public java.util.List<ProjectRepositoryResponse> listRepositories(String selectedJiraBaseUrl, String projectKey) {
        String baseUrl = jiraUrlFactory.normalizeBaseUrl(requireNonBlank(selectedJiraBaseUrl, "jiraBaseUrl"));
        String resolvedProjectKey = requireNonBlank(projectKey, "projectKey").trim();
        return projectServiceClient.listRepositories(resolvedProjectKey, baseUrl);
    }

    private ProjectRepositoryResponse resolveRepository(String projectKey, String baseUrl, Long repositoryId, String repositoryUrl) {
        if (repositoryId != null) {
            return projectServiceClient.getRepository(projectKey, repositoryId, baseUrl);
        }
        if (!isBlank(repositoryUrl)) {
            String selected = repositoryUrl.trim();
            return projectServiceClient.listRepositories(projectKey, baseUrl).stream()
                    .filter(ProjectRepositoryResponse::active)
                    .filter(repository -> selected.equalsIgnoreCase(repository.repoUrl()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Selected GitHub repository is not configured for project: " + projectKey));
        }
        return projectServiceClient.getDefaultRepository(projectKey, baseUrl);
    }

    private String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
