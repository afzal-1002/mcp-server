package com.myatos.net.mcp.github;

import com.myatos.net.mcp.config.GitHubCopilotProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GitHubUrlFactoryTest {

    @Test
    void usesPublicGitHubApiDefaults() {
        GitHubUrlFactory factory = new GitHubUrlFactory(properties("github.com", null));

        assertThat(factory.restUrl("/repos/my-org/my-repo/issues"))
                .isEqualTo("https://api.github.com/repos/my-org/my-repo/issues");
        assertThat(factory.graphqlUrl()).isEqualTo("https://api.github.com/graphql");
        assertThat(factory.repositoryWebUrl("my-org", "my-repo"))
                .isEqualTo("https://github.com/my-org/my-repo");
    }

    @Test
    void buildsEnterpriseUrlsFromHost() {
        GitHubUrlFactory factory = new GitHubUrlFactory(properties("https://github.enterprise.example/", null));

        assertThat(factory.restUrl("repos/my-org/my-repo/issues"))
                .isEqualTo("https://github.enterprise.example/api/v3/repos/my-org/my-repo/issues");
        assertThat(factory.graphqlUrl())
                .isEqualTo("https://github.enterprise.example/api/graphql");
        assertThat(factory.repositoryWebUrl("my-org", "my-repo"))
                .isEqualTo("https://github.enterprise.example/my-org/my-repo");
    }

    @Test
    void honorsExplicitApiBaseUrl() {
        GitHubUrlFactory factory = new GitHubUrlFactory(properties("github.enterprise.example", "https://api.custom.example/"));

        assertThat(factory.restUrl("/repos/my-org/my-repo/issues"))
                .isEqualTo("https://api.custom.example/repos/my-org/my-repo/issues");
        assertThat(factory.graphqlUrl()).isEqualTo("https://api.custom.example/graphql");
    }

    @Test
    void rejectsBlankRestPath() {
        GitHubUrlFactory factory = new GitHubUrlFactory(properties("github.com", null));

        assertThatThrownBy(() -> factory.restUrl(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("GitHub API path is required");
    }

    private GitHubCopilotProperties properties(String host, String apiBaseUrl) {
        return new GitHubCopilotProperties(
                host,
                apiBaseUrl,
                "my-org",
                "my-repo",
                "main",
                null,
                null,
                null,
                null,
                null
        );
    }
}
