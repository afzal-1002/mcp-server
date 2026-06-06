package com.myatos.net.mcp.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myatos.net.mcp.config.JiraMcpProperties;
import com.myatos.net.mcp.jira.JiraRestClient;
import com.myatos.net.mcp.jira.JiraUrlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JiraMcpToolsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private JiraRestClient jiraRestClient;

    private JiraMcpTools tools;

    @BeforeEach
    void setUp() {
        JiraMcpProperties properties = new JiraMcpProperties("3", 25);
        tools = new JiraMcpTools(new JiraUrlFactory(properties), jiraRestClient, properties, objectMapper);
    }

    @Test
    void callJiraApiDelegatesGenericDomainCall() {
        JsonNode response = objectMapper.createObjectNode().put("ok", true);
        when(jiraRestClient.exchange(any(), any(), any(), any(), any())).thenReturn(response);

        JsonNode result = tools.callJiraApi(
                "company.atlassian.net",
                "POST",
                "/issue",
                "user@example.com",
                "token",
                Map.of("summary", "Bug")
        );

        ArgumentCaptor<JsonNode> payloadCaptor = ArgumentCaptor.forClass(JsonNode.class);
        verify(jiraRestClient).exchange(
                eq("https://company.atlassian.net/rest/api/3/issue"),
                eq("POST"),
                eq("user@example.com"),
                eq("token"),
                payloadCaptor.capture()
        );
        assertThat(result).isSameAs(response);
        assertThat(payloadCaptor.getValue().get("summary").asText()).isEqualTo("Bug");
    }

    @Test
    void callConfiguredJiraApiUsesProvidedBaseUrlAndEndpointEnum() {
        JsonNode response = objectMapper.createObjectNode().put("id", "ABC");
        when(jiraRestClient.exchange(any(), any(), any(), any(), any())).thenReturn(response);

        JsonNode result = tools.callConfiguredJiraApi(
                "selected.atlassian.net",
                "GET",
                "PROJECT_ID_OR_KEY",
                new Object[]{"ABC"},
                null,
                null,
                null
        );

        verify(jiraRestClient).exchange(
                eq("https://selected.atlassian.net/rest/api/3/project/ABC"),
                eq("GET"),
                eq(null),
                eq(null),
                eq(null)
        );
        assertThat(result).isSameAs(response);
    }

    @Test
    void searchIssuesUsesDefaultMaxResultsWhenMissing() {
        when(jiraRestClient.exchange(any(), any(), any(), any(), any()))
                .thenReturn(objectMapper.createObjectNode());

        tools.searchIssues("company.atlassian.net", "project = ABC", null, "user@example.com", "token");

        ArgumentCaptor<JsonNode> payloadCaptor = ArgumentCaptor.forClass(JsonNode.class);
        verify(jiraRestClient).exchange(
                eq("https://company.atlassian.net/rest/api/3/search/jql"),
                eq("POST"),
                eq("user@example.com"),
                eq("token"),
                payloadCaptor.capture()
        );
        assertThat(payloadCaptor.getValue().get("jql").asText()).isEqualTo("project = ABC");
        assertThat(payloadCaptor.getValue().get("maxResults").asInt()).isEqualTo(25);
    }

    @Test
    void getIssueAndListProjectsUseExpectedJiraEndpoints() {
        when(jiraRestClient.exchange(any(), any(), any(), any(), any()))
                .thenReturn(objectMapper.createObjectNode());

        tools.getIssue("company.atlassian.net", "ABC-1", "user@example.com", "token");
        tools.listProjects("company.atlassian.net", "user@example.com", "token");

        verify(jiraRestClient).exchange(
                eq("https://company.atlassian.net/rest/api/3/issue/ABC-1"),
                eq("GET"),
                eq("user@example.com"),
                eq("token"),
                eq(null)
        );
        verify(jiraRestClient).exchange(
                eq("https://company.atlassian.net/rest/api/3/project/search"),
                eq("GET"),
                eq("user@example.com"),
                eq("token"),
                eq(null)
        );
    }
}
