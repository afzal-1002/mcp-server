package com.myatos.net.mcp.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.myatos.net.mcp.github.GitHubCopilotService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class GitHubCopilotMcpTools {

    private final GitHubCopilotService copilotService;

    public GitHubCopilotMcpTools(GitHubCopilotService copilotService) {
        this.copilotService = copilotService;
    }

    @Tool(name = "github_verify_copilot_agent", description = "Verify whether GitHub Copilot coding agent is assignable in the configured repository.")
    public JsonNode verifyCopilotAgent(
            @ToolParam(description = "GitHub owner or organization. Optional when configured.", required = false) String owner,
            @ToolParam(description = "GitHub repository name. Optional when configured.", required = false) String repository,
            @ToolParam(description = "GitHub API token. Optional when configured.", required = false) String githubToken
    ) {
        return copilotService.verifyCopilotAssignable(owner, repository, githubToken);
    }

    @Tool(name = "github_create_copilot_issue", description = "Create a GitHub issue and assign it to GitHub Copilot coding agent.")
    public JsonNode createCopilotIssue(
            @ToolParam(description = "GitHub owner or organization. Optional when configured.", required = false) String owner,
            @ToolParam(description = "GitHub repository name. Optional when configured.", required = false) String repository,
            @ToolParam(description = "GitHub issue title.") String title,
            @ToolParam(description = "GitHub issue body.") String body,
            @ToolParam(description = "GitHub API token. Optional when configured.", required = false) String githubToken,
            @ToolParam(description = "Target repository in OWNER/REPO form. Optional when configured.", required = false) String targetRepository,
            @ToolParam(description = "Base branch for Copilot work. Optional when configured.", required = false) String baseBranch,
            @ToolParam(description = "Additional instructions for Copilot.", required = false) String customInstructions,
            @ToolParam(description = "Custom agent identifier, if enabled for the organization.", required = false) String customAgent,
            @ToolParam(description = "Model name, if model selection is enabled.", required = false) String model
    ) {
        return copilotService.createCopilotIssue(owner, repository, title, body, githubToken, targetRepository, baseBranch, customInstructions, customAgent, model);
    }

    @Tool(name = "github_assign_issue_to_copilot", description = "Assign an existing GitHub issue to GitHub Copilot coding agent.")
    public JsonNode assignIssueToCopilot(
            @ToolParam(description = "GitHub owner or organization. Optional when configured.", required = false) String owner,
            @ToolParam(description = "GitHub repository name. Optional when configured.", required = false) String repository,
            @ToolParam(description = "Existing GitHub issue number.") Integer issueNumber,
            @ToolParam(description = "GitHub API token. Optional when configured.", required = false) String githubToken,
            @ToolParam(description = "Target repository in OWNER/REPO form. Optional when configured.", required = false) String targetRepository,
            @ToolParam(description = "Base branch for Copilot work. Optional when configured.", required = false) String baseBranch,
            @ToolParam(description = "Additional instructions for Copilot.", required = false) String customInstructions,
            @ToolParam(description = "Custom agent identifier, if enabled for the organization.", required = false) String customAgent,
            @ToolParam(description = "Model name, if model selection is enabled.", required = false) String model
    ) {
        return copilotService.assignIssueToCopilot(owner, repository, issueNumber, githubToken, targetRepository, baseBranch, customInstructions, customAgent, model);
    }

    @Tool(name = "github_create_copilot_issue_from_jira", description = "Fetch a Jira defect and create a GitHub issue assigned to GitHub Copilot coding agent.")
    public JsonNode createCopilotIssueFromJira(
            @ToolParam(description = "Jira domain or base URL.") String jiraDomain,
            @ToolParam(description = "Jira issue key.") String jiraIssueKey,
            @ToolParam(description = "Jira account email. Optional when configured.", required = false) String jiraEmail,
            @ToolParam(description = "Jira API token. Optional when configured.", required = false) String jiraApiToken,
            @ToolParam(description = "GitHub owner or organization. Optional when configured.", required = false) String owner,
            @ToolParam(description = "GitHub repository name. Optional when configured.", required = false) String repository,
            @ToolParam(description = "GitHub API token. Optional when configured.", required = false) String githubToken,
            @ToolParam(description = "Target repository in OWNER/REPO form. Optional when configured.", required = false) String targetRepository,
            @ToolParam(description = "Base branch for Copilot work. Optional when configured.", required = false) String baseBranch,
            @ToolParam(description = "Additional instructions for Copilot.", required = false) String customInstructions,
            @ToolParam(description = "Custom agent identifier, if enabled for the organization.", required = false) String customAgent,
            @ToolParam(description = "Model name, if model selection is enabled.", required = false) String model
    ) {
        return copilotService.createCopilotIssueFromJira(
                jiraDomain,
                jiraIssueKey,
                jiraEmail,
                jiraApiToken,
                owner,
                repository,
                githubToken,
                targetRepository,
                baseBranch,
                customInstructions,
                customAgent,
                model
        );
    }

    @Tool(name = "github_create_copilot_issue_from_selected_project", description = "Resolve Jira credentials, project GitHub repository, and GitHub token from the logged-in user's database records, then create a GitHub Copilot issue for the Jira defect.")
    public JsonNode createCopilotIssueFromSelectedProject(
            @ToolParam(description = "Selected Jira domain/base URL from the logged-in user's Jira sites.") String jiraBaseUrl,
            @ToolParam(description = "Selected Jira project key.") String projectKey,
            @ToolParam(description = "Selected GitHub repository config id. Optional; if omitted, repositoryUrl or default repository is used.", required = false) Long repositoryId,
            @ToolParam(description = "Selected GitHub repository URL. Optional; if omitted, default repository is used.", required = false) String repositoryUrl,
            @ToolParam(description = "Selected Jira issue key.") String jiraIssueKey,
            @ToolParam(description = "Additional instructions for Copilot.", required = false) String customInstructions,
            @ToolParam(description = "Custom agent identifier, if enabled for the organization.", required = false) String customAgent,
            @ToolParam(description = "Model name, if model selection is enabled.", required = false) String model
    ) {
        return copilotService.createCopilotIssueFromSelectedProject(
                jiraBaseUrl,
                projectKey,
                repositoryId,
                repositoryUrl,
                jiraIssueKey,
                customInstructions,
                customAgent,
                model
        );
    }
}
