package com.myatos.net.mcp.jira;

import com.fasterxml.jackson.databind.JsonNode;
import com.myatos.net.mcp.config.JiraMcpProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
public class JiraRestClient {

    private final RestTemplate restTemplate;

    public JiraRestClient() {
        this.restTemplate = new RestTemplate();
    }

    public JsonNode exchange(String url, String method, String email, String apiToken, JsonNode body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, basicAuth(resolveEmail(email), resolveApiToken(apiToken)));

        HttpEntity<JsonNode> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.valueOf(method.toUpperCase()), entity, JsonNode.class).getBody();
    }

    private String resolveEmail(String email) {
        if (email != null && !email.isBlank()) {
            return email.trim();
        }
        throw new IllegalArgumentException("Jira email is required");
    }

    private String resolveApiToken(String apiToken) {
        if (apiToken != null && !apiToken.isBlank()) {
            return apiToken.trim();
        }
        throw new IllegalArgumentException("Jira API token is required");
    }

    private String basicAuth(String email, String apiToken) {
        String raw = email + ":" + apiToken;
        return "Basic " + Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }
}
