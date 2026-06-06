package com.myatos.net.mcp.jira;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JiraApiEndpointTest {

    @Test
    void formatsEndpointPathsWithArguments() {
        assertThat(JiraApiEndpoint.ISSUE_BY_ID_OR_KEY.path("ABC-123"))
                .isEqualTo("/issue/ABC-123");
        assertThat(JiraApiEndpoint.ISSUE_COMMENT_BY_ID.path("ABC-123", "456"))
                .isEqualTo("/issue/ABC-123/comment/456");
    }

    @Test
    void buildsUrlWithNormalizedBaseUrlAndVersion() {
        String url = JiraApiEndpoint.PROJECT_ID_OR_KEY.buildUrl(
                "https://jira.example.com/",
                "3",
                "ABC"
        );

        assertThat(url).isEqualTo("https://jira.example.com/rest/api/3/project/ABC");
    }
}
