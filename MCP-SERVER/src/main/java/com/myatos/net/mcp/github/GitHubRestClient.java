package com.myatos.net.mcp.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.myatos.net.mcp.config.GitHubCopilotProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GitHubRestClient {

    private final RestTemplate restTemplate;
    private final GitHubCopilotProperties properties;

    public GitHubRestClient(GitHubCopilotProperties properties) {
        this.restTemplate = new RestTemplate();
        this.properties = properties;
    }

    public JsonNode exchange(String url, HttpMethod method, String token, JsonNode body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.valueOf("application/vnd.github+json")));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resolveToken(token));
        headers.set("X-GitHub-Api-Version", properties.apiVersion());

        HttpEntity<JsonNode> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, method, entity, JsonNode.class).getBody();
    }

    public JsonNode graphql(String url, String token, JsonNode body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.valueOf("application/vnd.github+json")));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resolveToken(token));
        headers.set("X-GitHub-Api-Version", properties.apiVersion());
        headers.set("GraphQL-Features", "issues_copilot_assignment_api_support,coding_agent_model_selection");

        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), JsonNode.class).getBody();
    }

    private String resolveToken(String token) {
        if (token != null && !token.isBlank()) {
            return token.trim();
        }
        throw new IllegalArgumentException("GitHub token is required");
    }
}
