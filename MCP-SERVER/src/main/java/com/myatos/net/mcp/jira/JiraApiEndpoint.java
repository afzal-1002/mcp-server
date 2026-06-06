package com.myatos.net.mcp.jira;

public enum JiraApiEndpoint {
    ME("/myself"),
    USER("/user"),
    USER_SEARCH("/user/search"),
    STATUS("/status"),
    SEARCH("/search"),
    SEARCH_JQL("/search/jql"),
    ISSUE("/issue"),
    ISSUE_BY_ID_OR_KEY("/issue/%s"),
    ISSUE_ASSIGNEE("/issue/%s/assignee"),
    ISSUE_CHANGELOG("/issue/%s/changelog"),
    ISSUE_CHANGELOG_LIST("/issue/%s/changelog/list"),
    ISSUE_ARCHIVE("/issue/archive"),
    ISSUE_UNARCHIVE("/issue/unarchive"),
    ISSUE_CREATEMETA("/issue/createmeta"),
    ISSUE_CREATEMETA_ISSUETYPES("/issue/createmeta/%s/issuetypes"),
    ISSUE_COMMENTS("/issue/%s/comment"),
    ISSUE_COMMENT_BY_ID("/issue/%s/comment/%s"),
    ISSUE_TYPE("/issuetype"),
    ISSUE_TYPE_PROJECT("/issuetype/project"),
    COMMENT("/comment"),
    COMMENT_LIST("/comment/list"),
    FIELD("/field"),
    PROJECT("/project"),
    PROJECT_ID_OR_KEY("/project/%s"),
    PROJECT_SEARCH("/project/search"),
    PROJECT_STATUSES("/project/%s/statuses"),
    PROJECT_VALIDATE_KEY("/projectvalidate/key"),
    PROJECT_VALIDATE_VALID_KEY("/projectvalidate/validProjectKey"),
    PROJECT_VALIDATE_VALID_NAME("/projectvalidate/validProjectName");

    public static final String PROJECT_EXPAND = String.join(",",
            "lead", "issueTypes", "components", "roles", "versions",
            "description", "insight", "projectKeys", "url", "permissions"
    );

    private final String path;

    JiraApiEndpoint(String path) {
        this.path = path;
    }

    public String path(Object... args) {
        return args == null || args.length == 0 ? path : path.formatted(args);
    }

    public String buildUrl(String baseUrl, String version, Object... args) {
        return normalizeBaseUrl(baseUrl) + "/rest/api/" + version + path(args);
    }

    private static String normalizeBaseUrl(String baseUrl) {
        String trimmed = baseUrl == null ? "" : baseUrl.trim();
        if (trimmed.endsWith("/")) {
            return trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}
