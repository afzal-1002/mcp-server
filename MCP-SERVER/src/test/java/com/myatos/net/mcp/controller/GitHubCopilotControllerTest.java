package com.myatos.net.mcp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myatos.net.mcp.dto.CopilotAssignRequest;
import com.myatos.net.mcp.dto.CopilotIssueRequest;
import com.myatos.net.mcp.dto.DynamicJiraCopilotIssueRequest;
import com.myatos.net.mcp.dto.JiraCopilotIssueRequest;
import com.myatos.net.mcp.github.DynamicCopilotContextService;
import com.myatos.net.mcp.github.GitHubCopilotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubCopilotControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private GitHubCopilotService copilotService;

    @Mock
    private DynamicCopilotContextService dynamicCopilotContextService;

    @Test
    void verifyDelegatesQueryParameters() {
        GitHubCopilotController controller = controller();
        JsonNode response = objectMapper.createObjectNode().put("ok", true);
        when(copilotService.verifyCopilotAssignable("owner", "repo", "token")).thenReturn(response);

        JsonNode result = controller.verify("owner", "repo", "token");

        assertThat(result).isSameAs(response);
        verify(copilotService).verifyCopilotAssignable("owner", "repo", "token");
    }

    @Test
    void createIssueDelegatesRequestBodyFields() {
        GitHubCopilotController controller = controller();
        JsonNode response = objectMapper.createObjectNode().put("number", 10);
        when(copilotService.createCopilotIssue(
                "owner",
                "repo",
                "title",
                "body",
                "token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        )).thenReturn(response);

        JsonNode result = controller.createIssue(new CopilotIssueRequest(
                "owner",
                "repo",
                "title",
                "body",
                "token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        ));

        assertThat(result).isSameAs(response);
        verify(copilotService).createCopilotIssue(
                "owner",
                "repo",
                "title",
                "body",
                "token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        );
    }

    @Test
    void assignIssueDelegatesRequestBodyFields() {
        GitHubCopilotController controller = controller();
        JsonNode response = objectMapper.createObjectNode().put("assigned", true);
        when(copilotService.assignIssueToCopilot(
                "owner",
                "repo",
                5,
                "token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        )).thenReturn(response);

        JsonNode result = controller.assignIssue(new CopilotAssignRequest(
                "owner",
                "repo",
                5,
                "token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        ));

        assertThat(result).isSameAs(response);
        verify(copilotService).assignIssueToCopilot(
                "owner",
                "repo",
                5,
                "token",
                "owner/repo",
                "main",
                "instructions",
                "agent",
                "model"
        );
    }

    @Test
    void createIssueFromJiraDelegatesRequestBodyFields() {
        GitHubCopilotController controller = controller();
        JsonNode response = objectMapper.createObjectNode().put("number", 22);
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

        JsonNode result = controller.createIssueFromJira(new JiraCopilotIssueRequest(
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
        ));

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

    @Test
    void createIssueFromSelectedProjectDelegatesRepositorySelection() {
        GitHubCopilotController controller = controller();
        JsonNode response = objectMapper.createObjectNode().put("number", 30);
        when(copilotService.createCopilotIssueFromSelectedProject(
                "https://jira.example.com",
                "ABC",
                7L,
                "https://github.gsissc.myatos.net/org/repo.git",
                "ABC-1",
                "instructions",
                "agent",
                "model"
        )).thenReturn(response);

        JsonNode result = controller.createIssueFromSelectedProject(new DynamicJiraCopilotIssueRequest(
                "https://jira.example.com",
                "ABC",
                7L,
                "https://github.gsissc.myatos.net/org/repo.git",
                "ABC-1",
                "instructions",
                "agent",
                "model"
        ));

        assertThat(result).isSameAs(response);
        verify(copilotService).createCopilotIssueFromSelectedProject(
                "https://jira.example.com",
                "ABC",
                7L,
                "https://github.gsissc.myatos.net/org/repo.git",
                "ABC-1",
                "instructions",
                "agent",
                "model"
        );
    }

    private GitHubCopilotController controller() {
        return new GitHubCopilotController(copilotService, dynamicCopilotContextService);
    }
}
