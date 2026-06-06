package com.myatos.net.mcp.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myatos.net.mcp.config.GitHubCopilotProperties;
import com.myatos.net.mcp.config.JiraMcpProperties;
import com.myatos.net.mcp.jira.JiraRestClient;
import com.myatos.net.mcp.jira.JiraUrlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubCopilotServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private GitHubRestClient gitHubRestClient;

    @Mock
    private JiraRestClient jiraRestClient;

    @Mock
    private DynamicCopilotContextService dynamicCopilotContextService;

    private GitHubCopilotService service;

    @BeforeEach
    void setUp() {
        GitHubCopilotProperties githubProperties = new GitHubCopilotProperties(
                "github.gsissc.myatos.net",
                null,
                "configured-owner",
                "configured-repo",
                "develop",
                null,
                "enterprise-agent",
                "gpt-5",
                "copilot-swe-agent[bot]",
                null
        );
        JiraMcpProperties jiraProperties = new JiraMcpProperties("3", 25);
        service = new GitHubCopilotService(
                githubProperties,
                new GitHubUrlFactory(githubProperties),
                gitHubRestClient,
                new JiraUrlFactory(jiraProperties),
                jiraRestClient,
                dynamicCopilotContextService,
                objectMapper
        );
    }

    @Test
    void createCopilotIssueBuildsGitHubIssuePayloadWithAgentAssignment() {
        JsonNode response = objectMapper.createObjectNode().put("number", 12);
        when(gitHubRestClient.exchange(any(), any(), any(), any())).thenReturn(response);

        JsonNode result = service.createCopilotIssue(
                null,
                null,
                "Fix login",
                "Defect details",
                null,
                null,
                null,
                "Run focused tests",
                null,
                null
        );

        ArgumentCaptor<JsonNode> payloadCaptor = ArgumentCaptor.forClass(JsonNode.class);
        verify(gitHubRestClient).exchange(
                eq("https://github.gsissc.myatos.net/api/v3/repos/configured-owner/configured-repo/issues"),
                eq(HttpMethod.POST),
                eq(null),
                payloadCaptor.capture()
        );

        JsonNode payload = payloadCaptor.getValue();
        assertThat(result).isSameAs(response);
        assertThat(payload.get("title").asText()).isEqualTo("Fix login");
        assertThat(payload.get("body").asText()).isEqualTo("Defect details");
        assertThat(payload.get("assignees")).hasSize(1);
        assertThat(payload.get("assignees").get(0).asText()).isEqualTo("copilot-swe-agent[bot]");
        assertThat(payload.at("/agent_assignment/target_repo").asText()).isEqualTo("configured-owner/configured-repo");
        assertThat(payload.at("/agent_assignment/base_branch").asText()).isEqualTo("develop");
        assertThat(payload.at("/agent_assignment/custom_instructions").asText()).isEqualTo("Run focused tests");
        assertThat(payload.at("/agent_assignment/custom_agent").asText()).isEqualTo("enterprise-agent");
        assertThat(payload.at("/agent_assignment/model").asText()).isEqualTo("gpt-5");
    }

    @Test
    void assignIssueToCopilotRejectsInvalidIssueNumber() {
        assertThatThrownBy(() -> service.assignIssueToCopilot(
                "owner",
                "repo",
                0,
                "token",
                null,
                null,
                null,
                null,
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("GitHub issue number must be greater than zero");
    }

    @Test
    void verifyCopilotAssignableCallsGraphqlWithSuggestedActorsQuery() {
        JsonNode response = objectMapper.createObjectNode().put("ok", true);
        when(gitHubRestClient.graphql(any(), any(), any())).thenReturn(response);

        JsonNode result = service.verifyCopilotAssignable("owner", "repo", "token");

        ArgumentCaptor<JsonNode> payloadCaptor = ArgumentCaptor.forClass(JsonNode.class);
        verify(gitHubRestClient).graphql(
                eq("https://github.gsissc.myatos.net/api/graphql"),
                eq("token"),
                payloadCaptor.capture()
        );

        assertThat(result).isSameAs(response);
        assertThat(payloadCaptor.getValue().get("query").asText()).contains("suggestedActors");
        assertThat(payloadCaptor.getValue().at("/variables/owner").asText()).isEqualTo("owner");
        assertThat(payloadCaptor.getValue().at("/variables/name").asText()).isEqualTo("repo");
    }

    @Test
    void createCopilotIssueFromJiraFetchesJiraIssueAndCreatesGitHubIssue() throws Exception {
        JsonNode jiraIssue = objectMapper.readTree("""
                {
                  "fields": {
                    "summary": "Login fails",
                    "status": { "name": "Open" },
                    "priority": { "name": "High" }
                  }
                }
                """);
        JsonNode githubResponse = objectMapper.createObjectNode().put("number", 99);
        when(jiraRestClient.exchange(any(), any(), any(), any(), any())).thenReturn(jiraIssue);
        when(gitHubRestClient.exchange(any(), any(), any(), any())).thenReturn(githubResponse);

        JsonNode result = service.createCopilotIssueFromJira(
                "jira.example.com",
                "ABC-123",
                "jira@example.com",
                "jira-token",
                "owner",
                "repo",
                "gh-token",
                "owner/repo",
                "main",
                "Keep the patch minimal",
                "agent",
                "model"
        );

        ArgumentCaptor<JsonNode> payloadCaptor = ArgumentCaptor.forClass(JsonNode.class);
        verify(jiraRestClient).exchange(
                eq("https://jira.example.com/rest/api/3/issue/ABC-123"),
                eq("GET"),
                eq("jira@example.com"),
                eq("jira-token"),
                eq(null)
        );
        verify(gitHubRestClient).exchange(
                eq("https://github.gsissc.myatos.net/api/v3/repos/owner/repo/issues"),
                eq(HttpMethod.POST),
                eq("gh-token"),
                payloadCaptor.capture()
        );

        JsonNode payload = payloadCaptor.getValue();
        assertThat(result).isSameAs(githubResponse);
        assertThat(payload.get("title").asText()).isEqualTo("[Jira ABC-123] Login fails");
        assertThat(payload.get("body").asText())
                .contains("Jira issue: ABC-123")
                .contains("Jira URL: https://jira.example.com/browse/ABC-123")
                .contains("Status: Open")
                .contains("Priority: High")
                .contains("Keep the patch minimal");
    }

    @Test
    void createCopilotIssueRequiresTitle() {
        assertThatThrownBy(() -> service.createCopilotIssue(
                "owner",
                "repo",
                " ",
                null,
                "token",
                null,
                null,
                null,
                null,
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("GitHub issue title is required");
    }

    @Test
    void createCopilotIssueFromSelectedProjectUsesDatabaseResolvedContext() throws Exception {
        DynamicCopilotContext context = new DynamicCopilotContext(
                "https://jira.example.com",
                "jira@example.com",
                "jira-token",
                "ABC",
                "owner",
                "repo",
                "owner/repo",
                "develop",
                "gh-token"
        );
        JsonNode jiraIssue = objectMapper.readTree("""
                {
                  "fields": {
                    "summary": "Dynamic defect"
                  }
                }
                """);
        JsonNode githubResponse = objectMapper.createObjectNode().put("number", 100);
        when(dynamicCopilotContextService.resolve("jira.example.com", "ABC", 10L, "https://github.gsissc.myatos.net/owner/repo.git")).thenReturn(context);
        when(jiraRestClient.exchange(any(), any(), any(), any(), any())).thenReturn(jiraIssue);
        when(gitHubRestClient.exchange(any(), any(), any(), any())).thenReturn(githubResponse);

        JsonNode result = service.createCopilotIssueFromSelectedProject(
                "jira.example.com",
                "ABC",
                10L,
                "https://github.gsissc.myatos.net/owner/repo.git",
                "ABC-9",
                "Use repository tests",
                null,
                null
        );

        verify(jiraRestClient).exchange(
                eq("https://jira.example.com/rest/api/3/issue/ABC-9"),
                eq("GET"),
                eq("jira@example.com"),
                eq("jira-token"),
                eq(null)
        );
        verify(gitHubRestClient).exchange(
                eq("https://github.gsissc.myatos.net/api/v3/repos/owner/repo/issues"),
                eq(HttpMethod.POST),
                eq("gh-token"),
                any()
        );
        assertThat(result).isSameAs(githubResponse);
    }
}
