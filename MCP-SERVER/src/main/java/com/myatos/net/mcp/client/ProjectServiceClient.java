package com.myatos.net.mcp.client;

import com.myatos.net.mcp.client.dto.ProjectRepositoryResponse;
import com.myatos.net.mcp.config.McpServiceProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class ProjectServiceClient {

    private final McpServiceProperties properties;
    private final ServiceHttpClient serviceHttpClient;

    public ProjectServiceClient(McpServiceProperties properties, ServiceHttpClient serviceHttpClient) {
        this.properties = properties;
        this.serviceHttpClient = serviceHttpClient;
    }

    public ProjectRepositoryResponse getDefaultRepository(String projectKey, String jiraBaseUrl) {
        String encodedProjectKey = UriUtils.encodePathSegment(projectKey, StandardCharsets.UTF_8);
        String url = UriComponentsBuilder
                .fromUriString(properties.projectServiceUrl() + "/api/wut/projects/" + encodedProjectKey + "/repositories/default")
                .queryParam("baseUrl", jiraBaseUrl)
                .toUriString();
        return serviceHttpClient.get(url, ProjectRepositoryResponse.class);
    }

    public ProjectRepositoryResponse getRepository(String projectKey, Long repositoryId, String jiraBaseUrl) {
        String encodedProjectKey = UriUtils.encodePathSegment(projectKey, StandardCharsets.UTF_8);
        String url = UriComponentsBuilder
                .fromUriString(properties.projectServiceUrl() + "/api/wut/projects/" + encodedProjectKey + "/repositories/" + repositoryId)
                .queryParam("baseUrl", jiraBaseUrl)
                .toUriString();
        return serviceHttpClient.get(url, ProjectRepositoryResponse.class);
    }

    public List<ProjectRepositoryResponse> listRepositories(String projectKey, String jiraBaseUrl) {
        String encodedProjectKey = UriUtils.encodePathSegment(projectKey, StandardCharsets.UTF_8);
        String url = UriComponentsBuilder
                .fromUriString(properties.projectServiceUrl() + "/api/wut/projects/" + encodedProjectKey + "/repositories")
                .queryParam("baseUrl", jiraBaseUrl)
                .toUriString();
        ProjectRepositoryResponse[] response = serviceHttpClient.get(url, ProjectRepositoryResponse[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }
}
