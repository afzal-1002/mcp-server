package com.myatos.net.mcp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myatos.net.mcp.config.JiraMcpProperties;
import com.myatos.net.mcp.dto.ConfiguredJiraApiCallRequest;
import com.myatos.net.mcp.dto.JiraApiCallRequest;
import com.myatos.net.mcp.jira.JiraRestClient;
import com.myatos.net.mcp.jira.JiraUrlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JiraMcpControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private JiraRestClient jiraRestClient;

    private JiraMcpController controller;

    @BeforeEach
    void setUp() {
        JiraMcpProperties properties = new JiraMcpProperties("3", 25);
        controller = new JiraMcpController(new JiraUrlFactory(properties), jiraRestClient);
    }

    @Test
    void callDelegatesGenericRequest() {
        JsonNode body = objectMapper.createObjectNode().put("summary", "Bug");
        JsonNode response = objectMapper.createObjectNode().put("ok", true);
        when(jiraRestClient.exchange(any(), any(), any(), any(), any())).thenReturn(response);

        JsonNode result = controller.call(new JiraApiCallRequest(
                "company.atlassian.net",
                "POST",
                "/issue",
                "user@example.com",
                "token",
                body
        ));

        assertThat(result).isSameAs(response);
        verify(jiraRestClient).exchange(
                eq("https://company.atlassian.net/rest/api/3/issue"),
                eq("POST"),
                eq("user@example.com"),
                eq("token"),
                eq(body)
        );
    }

    @Test
    void callConfiguredDelegatesSelectedBaseUrlEndpointRequest() {
        JsonNode response = objectMapper.createObjectNode().put("key", "ABC");
        when(jiraRestClient.exchange(any(), any(), any(), any(), any())).thenReturn(response);

        JsonNode result = controller.callConfigured(new ConfiguredJiraApiCallRequest(
                "selected.atlassian.net",
                "GET",
                "PROJECT_ID_OR_KEY",
                null,
                null,
                null,
                new Object[]{"ABC"}
        ));

        assertThat(result).isSameAs(response);
        verify(jiraRestClient).exchange(
                eq("https://selected.atlassian.net/rest/api/3/project/ABC"),
                eq("GET"),
                eq(null),
                eq(null),
                eq(null)
        );
    }

    @Test
    void endpointsReturnsKnownEndpointNames() {
        List<String> endpoints = controller.endpoints();

        assertThat(endpoints)
                .contains("ISSUE_BY_ID_OR_KEY", "PROJECT_SEARCH", "SEARCH_JQL");
    }
}
