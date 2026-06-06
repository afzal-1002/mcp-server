package com.myatos.net.mcp.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record JiraApiCallRequest(
        String domain,
        String method,
        String apiPath,
        String email,
        String apiToken,
        JsonNode body
) {
}
