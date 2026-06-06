package com.myatos.net.mcp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.myatos.net.mcp.dto.CopilotAssignRequest;
import com.myatos.net.mcp.dto.CopilotIssueRequest;
import com.myatos.net.mcp.dto.DynamicJiraCopilotIssueRequest;
import com.myatos.net.mcp.dto.JiraCopilotIssueRequest;
import com.myatos.net.mcp.dto.ProjectRepositoriesRequest;
import com.myatos.net.mcp.github.GitHubCopilotService;
import com.myatos.net.mcp.github.DynamicCopilotContextService;
import com.myatos.net.mcp.client.dto.ProjectRepositoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wut/mcp/github/copilot")
public class GitHubCopilotController {

    private final GitHubCopilotService copilotService;
    private final DynamicCopilotContextService dynamicCopilotContextService;

    public GitHubCopilotController(GitHubCopilotService copilotService, DynamicCopilotContextService dynamicCopilotContextService) {
        this.copilotService = copilotService;
        this.dynamicCopilotContextService = dynamicCopilotContextService;
    }

    @GetMapping("/verify")
    public JsonNode verify(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String repository,
            @RequestParam(required = false) String githubToken
    ) {
        return copilotService.verifyCopilotAssignable(owner, repository, githubToken);
    }

    @PostMapping("/issues")
    public JsonNode createIssue(@RequestBody CopilotIssueRequest request) {
        return copilotService.createCopilotIssue(
                request.owner(),
                request.repository(),
                request.title(),
                request.body(),
                request.githubToken(),
                request.targetRepository(),
                request.baseBranch(),
                request.customInstructions(),
                request.customAgent(),
                request.model()
        );
    }

    @PostMapping("/issues/assign")
    public JsonNode assignIssue(@RequestBody CopilotAssignRequest request) {
        return copilotService.assignIssueToCopilot(
                request.owner(),
                request.repository(),
                request.issueNumber(),
                request.githubToken(),
                request.targetRepository(),
                request.baseBranch(),
                request.customInstructions(),
                request.customAgent(),
                request.model()
        );
    }

    @PostMapping("/issues/from-jira")
    public JsonNode createIssueFromJira(@RequestBody JiraCopilotIssueRequest request) {
        return copilotService.createCopilotIssueFromJira(
                request.jiraDomain(),
                request.jiraIssueKey(),
                request.jiraEmail(),
                request.jiraApiToken(),
                request.owner(),
                request.repository(),
                request.githubToken(),
                request.targetRepository(),
                request.baseBranch(),
                request.customInstructions(),
                request.customAgent(),
                request.model()
        );
    }

    @PostMapping("/issues/from-selected-project")
    public JsonNode createIssueFromSelectedProject(@RequestBody DynamicJiraCopilotIssueRequest request) {
        return copilotService.createCopilotIssueFromSelectedProject(
                request.jiraBaseUrl(),
                request.projectKey(),
                request.repositoryId(),
                request.repositoryUrl(),
                request.jiraIssueKey(),
                request.customInstructions(),
                request.customAgent(),
                request.model()
        );
    }

    @PostMapping("/repositories")
    public java.util.List<ProjectRepositoryResponse> listProjectRepositories(@RequestBody ProjectRepositoriesRequest request) {
        return dynamicCopilotContextService.listRepositories(request.jiraBaseUrl(), request.projectKey());
    }
}
