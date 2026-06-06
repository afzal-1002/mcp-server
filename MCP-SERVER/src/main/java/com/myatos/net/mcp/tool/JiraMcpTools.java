package com.myatos.net.mcp.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myatos.net.mcp.config.JiraMcpProperties;
import com.myatos.net.mcp.jira.JiraApiEndpoint;
import com.myatos.net.mcp.jira.JiraRestClient;
import com.myatos.net.mcp.jira.JiraUrlFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JiraMcpTools {

    private final JiraUrlFactory urlFactory;
    private final JiraRestClient jiraRestClient;
    private final JiraMcpProperties properties;
    private final ObjectMapper objectMapper;

    public JiraMcpTools(
            JiraUrlFactory urlFactory,
            JiraRestClient jiraRestClient,
            JiraMcpProperties properties,
            ObjectMapper objectMapper
    ) {
        this.urlFactory = urlFactory;
        this.jiraRestClient = jiraRestClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Tool(
            name = "jira_call_api",
            description = "Call any Jira REST API endpoint under a provided Jira domain or base URL. Use apiPath either as '/issue/KEY' or '/rest/api/3/issue/KEY'."
    )
    public JsonNode callJiraApi(
            @ToolParam(description = "Jira domain or base URL, for example 'company.atlassian.net' or 'https://company.atlassian.net'.") String domain,
            @ToolParam(description = "HTTP method: GET, POST, PUT, PATCH, or DELETE.") String method,
            @ToolParam(description = "Jira REST API path under the domain.") String apiPath,
            @ToolParam(description = "Jira account email. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String email,
            @ToolParam(description = "Jira API token. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String apiToken,
            @ToolParam(description = "JSON request body for POST, PUT, or PATCH. Use null for GET/DELETE.", required = false) Map<String, Object> body
    ) {
        JsonNode payload = body == null ? null : objectMapper.valueToTree(body);
        return jiraRestClient.exchange(urlFactory.genericUrl(domain, apiPath), method, email, apiToken, payload);
    }

    @Tool(
            name = "jira_call_configured_api",
            description = "Call Jira through a selected Jira base URL using a JiraApiEndpoint enum name such as PROJECT_SEARCH, SEARCH_JQL, or ISSUE_BY_ID_OR_KEY."
    )
    public JsonNode callConfiguredJiraApi(
            @ToolParam(description = "Selected Jira domain or base URL.") String baseUrl,
            @ToolParam(description = "HTTP method: GET, POST, PUT, PATCH, or DELETE.") String method,
            @ToolParam(description = "JiraApiEndpoint enum name.") String endpoint,
            @ToolParam(description = "Optional path arguments for formatted endpoints such as ISSUE_BY_ID_OR_KEY.", required = false) Object[] pathArgs,
            @ToolParam(description = "Jira account email. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String email,
            @ToolParam(description = "Jira API token. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String apiToken,
            @ToolParam(description = "JSON request body for POST, PUT, or PATCH. Use null for GET/DELETE.", required = false) Map<String, Object> body
    ) {
        JiraApiEndpoint apiEndpoint = JiraApiEndpoint.valueOf(endpoint.trim().toUpperCase());
        JsonNode payload = body == null ? null : objectMapper.valueToTree(body);
        return jiraRestClient.exchange(urlFactory.configuredUrl(baseUrl, apiEndpoint, pathArgs), method, email, apiToken, payload);
    }

    @Tool(name = "jira_get_issue", description = "Fetch a Jira issue by key from a provided Jira domain.")
    public JsonNode getIssue(
            @ToolParam(description = "Jira domain or base URL.") String domain,
            @ToolParam(description = "Jira issue key, for example PROJ-123.") String issueKey,
            @ToolParam(description = "Jira account email. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String email,
            @ToolParam(description = "Jira API token. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String apiToken
    ) {
        String path = JiraApiEndpoint.ISSUE_BY_ID_OR_KEY.path(issueKey);
        return jiraRestClient.exchange(urlFactory.genericUrl(domain, path), "GET", email, apiToken, null);
    }

    @Tool(name = "jira_search_issues", description = "Search Jira issues with JQL on a provided Jira domain.")
    public JsonNode searchIssues(
            @ToolParam(description = "Jira domain or base URL.") String domain,
            @ToolParam(description = "JQL query.") String jql,
            @ToolParam(description = "Maximum number of results.", required = false) Integer maxResults,
            @ToolParam(description = "Jira account email. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String email,
            @ToolParam(description = "Jira API token. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String apiToken
    ) {
        Map<String, Object> payload = Map.of(
                "jql", jql,
                "maxResults", maxResults == null ? properties.defaultMaxResults() : maxResults
        );
        return jiraRestClient.exchange(
                urlFactory.genericUrl(domain, JiraApiEndpoint.SEARCH_JQL.path()),
                "POST",
                email,
                apiToken,
                objectMapper.valueToTree(payload)
        );
    }

    @Tool(name = "jira_list_projects", description = "List Jira projects from a provided Jira domain.")
    public JsonNode listProjects(
            @ToolParam(description = "Jira domain or base URL.") String domain,
            @ToolParam(description = "Jira account email. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String email,
            @ToolParam(description = "Jira API token. Required for direct calls. Prefer dynamic selected-project tools for database-resolved credentials.", required = false) String apiToken
    ) {
        return jiraRestClient.exchange(urlFactory.genericUrl(domain, JiraApiEndpoint.PROJECT_SEARCH.path()), "GET", email, apiToken, null);
    }
}
