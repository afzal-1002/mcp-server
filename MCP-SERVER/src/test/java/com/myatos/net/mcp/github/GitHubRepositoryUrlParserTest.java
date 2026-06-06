package com.myatos.net.mcp.github;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GitHubRepositoryUrlParserTest {

    private final GitHubRepositoryUrlParser parser = new GitHubRepositoryUrlParser();

    @Test
    void parsesHttpsRepositoryUrl() {
        GitHubRepositoryUrlParser.ParsedRepository parsed =
                parser.parse("https://github.gsissc.myatos.net/my-org/my-repo.git");

        assertThat(parsed.owner()).isEqualTo("my-org");
        assertThat(parsed.repository()).isEqualTo("my-repo");
        assertThat(parsed.fullName()).isEqualTo("my-org/my-repo");
    }

    @Test
    void parsesSshRepositoryUrl() {
        GitHubRepositoryUrlParser.ParsedRepository parsed =
                parser.parse("git@github.gsissc.myatos.net:my-org/my-repo.git");

        assertThat(parsed.owner()).isEqualTo("my-org");
        assertThat(parsed.repository()).isEqualTo("my-repo");
    }

    @Test
    void rejectsBlankRepositoryUrl() {
        assertThatThrownBy(() -> parser.parse(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("GitHub repository URL is required");
    }
}
