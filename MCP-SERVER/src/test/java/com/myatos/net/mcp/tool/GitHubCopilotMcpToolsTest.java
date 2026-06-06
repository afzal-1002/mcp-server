package com.myatos.net.mcp.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myatos.net.mcp.github.GitHubCopilotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubCopilotMcpToolsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private GitHubCopilotService copilotService;

    @Test
    void verifyToolDelegatesToService() {
        GitHubCopilotMcpTools tools = new GitHubCopilotMcpTools(copilotService);
        JsonNode response = objectMapper.createObjectNode().put("ok", true);
        when(copilotService.verifyCopilotAssignable("owner", "repo", "token")).thenReturn(response);

        JsonNode result = tools.verifyCopilotAgent("owner", "repo", "token");

        assertThat(result).isSameAs(response);
        verify(copilotService).verifyCopilotAssignable("owner", "repo", "token");
    }

    @Test
    void createIssueFromJiraToolDelegatesEveryParameter() {
        GitHubCopilotMcpTools tools = new GitHubCopilotMcpTools(copilotService);
        JsonNode response = objectMapper.createObjectNode().put("number", 1);
        when(copilotService.createCopilotIssueFromJira(
                "jira.example.com",
                "ABC-1",
                "jira@example.com",
                "jira-token",
                "owner",
                "repo",
                "gh-token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        )).thenReturn(response);

        JsonNode result = tools.createCopilotIssueFromJira(
                "jira.example.com",
                "ABC-1",
                "jira@example.com",
                "jira-token",
                "owner",
                "repo",
                "gh-token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        );

        assertThat(result).isSameAs(response);
        verify(copilotService).createCopilotIssueFromJira(
                "jira.example.com",
                "ABC-1",
                "jira@example.com",
                "jira-token",
                "owner",
                "repo",
                "gh-token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        );
    }
}
