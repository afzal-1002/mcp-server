package com.myatos.net.mcp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.myatos.net.mcp.dto.ConfiguredJiraApiCallRequest;
import com.myatos.net.mcp.dto.JiraApiCallRequest;
import com.myatos.net.mcp.jira.JiraApiEndpoint;
import com.myatos.net.mcp.jira.JiraRestClient;
import com.myatos.net.mcp.jira.JiraUrlFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/wut/mcp/jira")
public class JiraMcpController {

    private final JiraUrlFactory urlFactory;
    private final JiraRestClient jiraRestClient;

    public JiraMcpController(JiraUrlFactory urlFactory, JiraRestClient jiraRestClient) {
        this.urlFactory = urlFactory;
        this.jiraRestClient = jiraRestClient;
    }

    @PostMapping("/call")
    public JsonNode call(@RequestBody JiraApiCallRequest request) {
        return jiraRestClient.exchange(
                urlFactory.genericUrl(request.domain(), request.apiPath()),
                request.method(),
                request.email(),
                request.apiToken(),
                request.body()
        );
    }

    @PostMapping("/configured/call")
    public JsonNode callConfigured(@RequestBody ConfiguredJiraApiCallRequest request) {
        JiraApiEndpoint endpoint = JiraApiEndpoint.valueOf(request.endpoint().trim().toUpperCase());
        return jiraRestClient.exchange(
                urlFactory.configuredUrl(request.baseUrl(), endpoint, request.pathArgs()),
                request.method(),
                request.email(),
                request.apiToken(),
                request.body()
        );
    }

    @GetMapping("/configured/endpoints")
    public List<String> endpoints() {
        return Arrays.stream(JiraApiEndpoint.values()).map(Enum::name).toList();
    }
}
