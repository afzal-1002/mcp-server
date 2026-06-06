package com.myatos.net.mcp.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myatos.net.mcp.config.GitHubCopilotProperties;
import com.myatos.net.mcp.jira.JiraApiEndpoint;
import com.myatos.net.mcp.jira.JiraRestClient;
import com.myatos.net.mcp.jira.JiraUrlFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitHubCopilotService {

    private final GitHubCopilotProperties properties;
    private final GitHubUrlFactory gitHubUrlFactory;
    private final GitHubRestClient gitHubRestClient;
    private final JiraUrlFactory jiraUrlFactory;
    private final JiraRestClient jiraRestClient;
    private final DynamicCopilotContextService dynamicCopilotContextService;
    private final ObjectMapper objectMapper;

    public GitHubCopilotService(
            GitHubCopilotProperties properties,
            GitHubUrlFactory gitHubUrlFactory,
            GitHubRestClient gitHubRestClient,
            JiraUrlFactory jiraUrlFactory,
            JiraRestClient jiraRestClient,
            DynamicCopilotContextService dynamicCopilotContextService,
            ObjectMapper objectMapper
    ) {
        this.properties = properties;
        this.gitHubUrlFactory = gitHubUrlFactory;
        this.gitHubRestClient = gitHubRestClient;
        this.jiraUrlFactory = jiraUrlFactory;
        this.jiraRestClient = jiraRestClient;
        this.dynamicCopilotContextService = dynamicCopilotContextService;
        this.objectMapper = objectMapper;
    }

    public JsonNode createCopilotIssue(
            String owner,
            String repository,
            String title,
            String body,
            String githubToken,
            String targetRepository,
            String baseBranch,
            String customInstructions,
            String customAgent,
            String model
    ) {
        String resolvedOwner = resolve(owner, properties.owner(), "GitHub owner");
        String resolvedRepository = resolve(repository, properties.repository(), "GitHub repository");
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("title", resolveRequired(title, "GitHub issue title"));
        payload.put("body", body == null ? "" : body);
        payload.put("assignees", List.of(properties.copilotAssignee()));
        payload.put("agent_assignment", agentAssignment(
                resolvedOwner,
                resolvedRepository,
                targetRepository,
                baseBranch,
                customInstructions,
                customAgent,
                model
        ));

        return gitHubRestClient.exchange(
                gitHubUrlFactory.restUrl("/repos/%s/%s/issues".formatted(resolvedOwner, resolvedRepository)),
                HttpMethod.POST,
                githubToken,
                objectMapper.valueToTree(payload)
        );
    }

    public JsonNode assignIssueToCopilot(
            String owner,
            String repository,
            Integer issueNumber,
            String githubToken,
            String targetRepository,
            String baseBranch,
            String customInstructions,
            String customAgent,
            String model
    ) {
        String resolvedOwner = resolve(owner, properties.owner(), "GitHub owner");
        String resolvedRepository = resolve(repository, properties.repository(), "GitHub repository");
        if (issueNumber == null || issueNumber <= 0) {
            throw new IllegalArgumentException("GitHub issue number must be greater than zero");
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("assignees", List.of(properties.copilotAssignee()));
        payload.put("agent_assignment", agentAssignment(
                resolvedOwner,
                resolvedRepository,
                targetRepository,
                baseBranch,
                customInstructions,
                customAgent,
                model
        ));

        return gitHubRestClient.exchange(
                gitHubUrlFactory.restUrl("/repos/%s/%s/issues/%d/assignees".formatted(resolvedOwner, resolvedRepository, issueNumber)),
                HttpMethod.POST,
                githubToken,
                objectMapper.valueToTree(payload)
        );
    }

    public JsonNode verifyCopilotAssignable(String owner, String repository, String githubToken) {
        String resolvedOwner = resolve(owner, properties.owner(), "GitHub owner");
        String resolvedRepository = resolve(repository, properties.repository(), "GitHub repository");
        Map<String, Object> payload = Map.of(
                "query", """
                        query($owner: String!, $name: String!) {
                          repository(owner: $owner, name: $name) {
                            suggestedActors(capabilities: [CAN_BE_ASSIGNED], first: 100) {
                              nodes {
                                login
                                __typename
                                ... on Bot { id }
                                ... on User { id }
                              }
                            }
                          }
                        }
                        """,
                "variables", Map.of("owner", resolvedOwner, "name", resolvedRepository)
        );
        return gitHubRestClient.graphql(gitHubUrlFactory.graphqlUrl(), githubToken, objectMapper.valueToTree(payload));
    }

    public JsonNode createCopilotIssueFromJira(
            String jiraDomain,
            String jiraIssueKey,
            String jiraEmail,
            String jiraApiToken,
            String owner,
            String repository,
            String githubToken,
            String targetRepository,
            String baseBranch,
            String customInstructions,
            String customAgent,
            String model
    ) {
        String issueKey = resolveRequired(jiraIssueKey, "Jira issue key");
        JsonNode jiraIssue = jiraRestClient.exchange(
                jiraUrlFactory.genericUrl(jiraDomain, JiraApiEndpoint.ISSUE_BY_ID_OR_KEY.path(issueKey)),
                "GET",
                jiraEmail,
                jiraApiToken,
                null
        );

        String title = "[Jira " + issueKey + "] " + extractText(jiraIssue, "/fields/summary", "Fix Jira defect " + issueKey);
        String body = buildJiraIssueBody(jiraDomain, issueKey, jiraIssue, customInstructions);

        return createCopilotIssue(
                owner,
                repository,
                title,
                body,
                githubToken,
                targetRepository,
                baseBranch,
                customInstructions,
                customAgent,
                model
        );
    }

    public JsonNode createCopilotIssueFromSelectedProject(
            String jiraBaseUrl,
            String projectKey,
            Long repositoryId,
            String repositoryUrl,
            String jiraIssueKey,
            String customInstructions,
            String customAgent,
            String model
    ) {
        DynamicCopilotContext context = dynamicCopilotContextService.resolve(jiraBaseUrl, projectKey, repositoryId, repositoryUrl);
        return createCopilotIssueFromJira(
                context.jiraBaseUrl(),
                jiraIssueKey,
                context.jiraEmail(),
                context.jiraApiToken(),
                context.repositoryOwner(),
                context.repositoryName(),
                context.githubToken(),
                context.targetRepository(),
                context.baseBranch(),
                customInstructions,
                customAgent,
                model
        );
    }

    private Map<String, Object> agentAssignment(
            String owner,
            String repository,
            String targetRepository,
            String baseBranch,
            String customInstructions,
            String customAgent,
            String model
    ) {
        Map<String, Object> assignment = new LinkedHashMap<>();
        assignment.put("target_repo", resolve(targetRepository, properties.targetRepository(), owner + "/" + repository));
        assignment.put("base_branch", resolve(baseBranch, properties.baseBranch(), "main"));
        assignment.put("custom_instructions", customInstructions == null ? "" : customInstructions);
        assignment.put("custom_agent", resolveOptional(customAgent, properties.customAgent()));
        assignment.put("model", resolveOptional(model, properties.model()));
        return assignment;
    }

    private String buildJiraIssueBody(String jiraDomain, String issueKey, JsonNode jiraIssue, String customInstructions) {
        String jiraUrl = jiraUrlFactory.normalizeBaseUrl(jiraDomain) + "/browse/" + issueKey;
        StringBuilder body = new StringBuilder();
        body.append("## Jira defect\n\n");
        body.append("- Jira issue: ").append(issueKey).append("\n");
        body.append("- Jira URL: ").append(jiraUrl).append("\n");
        body.append("- Status: ").append(extractText(jiraIssue, "/fields/status/name", "unknown")).append("\n");
        body.append("- Priority: ").append(extractText(jiraIssue, "/fields/priority/name", "unknown")).append("\n\n");
        body.append("## Task\n\n");
        body.append("Read the repository code, reproduce the defect from the Jira context, implement the smallest suitable fix, add or update tests where practical, and open a draft pull request.\n\n");
        if (customInstructions != null && !customInstructions.isBlank()) {
            body.append("## Additional instructions\n\n").append(customInstructions.trim()).append("\n\n");
        }
        body.append("## Jira issue payload\n\n```json\n");
        body.append(jiraIssue == null ? "{}" : jiraIssue.toPrettyString());
        body.append("\n```\n");
        return body.toString();
    }

    private String extractText(JsonNode node, String pointer, String fallback) {
        if (node == null) {
            return fallback;
        }
        JsonNode value = node.at(pointer);
        if (value.isMissingNode() || value.isNull()) {
            return fallback;
        }
        return value.isTextual() ? value.asText() : value.toString();
    }

    private String resolve(String explicit, String configured, String fieldName) {
        if (explicit != null && !explicit.isBlank()) {
            return explicit.trim();
        }
        return resolveRequired(configured, fieldName);
    }

    private String resolveOptional(String explicit, String configured) {
        if (explicit != null && !explicit.isBlank()) {
            return explicit.trim();
        }
        return configured == null ? "" : configured.trim();
    }

    private String resolveRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }
}
