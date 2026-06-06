package com.myatos.net.mcp.client;

import com.myatos.net.mcp.client.dto.ResolvedIntegrationCredentialResponse;
import com.myatos.net.mcp.client.dto.UserCredentialResponse;
import com.myatos.net.mcp.config.McpServiceProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UserServiceClient {

    private final McpServiceProperties properties;
    private final ServiceHttpClient serviceHttpClient;

    public UserServiceClient(McpServiceProperties properties, ServiceHttpClient serviceHttpClient) {
        this.properties = properties;
        this.serviceHttpClient = serviceHttpClient;
    }

    public UserCredentialResponse getResolvedJiraCredential(String selectedBaseUrl) {
        String url = UriComponentsBuilder
                .fromUriString(properties.userServiceUrl() + "/api/wut/credentials/me/resolved")
                .queryParam("baseUrl", selectedBaseUrl)
                .toUriString();
        return serviceHttpClient.get(url, UserCredentialResponse.class);
    }

    public ResolvedIntegrationCredentialResponse getResolvedIntegrationCredential(Long credentialId) {
        String url = properties.userServiceUrl() + "/api/wut/integration-credentials/" + credentialId + "/resolved";
        return serviceHttpClient.get(url, ResolvedIntegrationCredentialResponse.class);
    }
}
