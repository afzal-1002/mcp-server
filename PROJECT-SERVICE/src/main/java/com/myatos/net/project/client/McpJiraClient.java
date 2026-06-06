package com.myatos.net.project.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.myatos.net.project.configuration.FeignSecurityConfiguration;
import com.myatos.net.project.dto.mcp.McpConfiguredJiraApiCallRequest;
import com.myatos.net.project.dto.mcp.McpJiraApiCallRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "MCP-SERVER",
        contextId = "mcpJiraClient",
        path = "/api/wut/mcp/jira",
        configuration = FeignSecurityConfiguration.class
)
public interface McpJiraClient {

    @PostMapping("/call")
    JsonNode call(@RequestBody McpJiraApiCallRequest request);

    @PostMapping("/configured/call")
    JsonNode callConfigured(@RequestBody McpConfiguredJiraApiCallRequest request);
}
