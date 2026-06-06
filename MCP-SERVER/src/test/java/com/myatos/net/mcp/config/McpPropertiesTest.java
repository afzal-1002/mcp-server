package com.myatos.net.mcp.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class McpPropertiesTest {

    @Test
    void jiraPropertiesUseSafeDefaultsWhenValuesAreMissing() {
        JiraMcpProperties properties = new JiraMcpProperties(null, 0);

        assertThat(properties.apiVersion()).isEqualTo("3");
        assertThat(properties.defaultMaxResults()).isEqualTo(25);
    }

    @Test
    void jiraPropertiesKeepConfiguredValues() {
        JiraMcpProperties properties = new JiraMcpProperties("2", 100);

        assertThat(properties.apiVersion()).isEqualTo("2");
        assertThat(properties.defaultMaxResults()).isEqualTo(100);
    }

    @Test
    void githubPropertiesUseDefaultsAndBuildTargetRepository() {
        GitHubCopilotProperties properties = new GitHubCopilotProperties(
                null,
                null,
                "my-org",
                "my-repo",
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(properties.host()).isEqualTo("github.com");
        assertThat(properties.baseBranch()).isEqualTo("main");
        assertThat(properties.targetRepository()).isEqualTo("my-org/my-repo");
        assertThat(properties.copilotAssignee()).isEqualTo("copilot-swe-agent[bot]");
        assertThat(properties.apiVersion()).isEqualTo("2022-11-28");
    }
}
