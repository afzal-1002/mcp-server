package com.myatos.net.mcp.jira;

import com.myatos.net.mcp.config.JiraMcpProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JiraUrlFactoryTest {

    private final JiraMcpProperties properties = new JiraMcpProperties("3", 50);

    private final JiraUrlFactory urlFactory = new JiraUrlFactory(properties);

    @Test
    void genericUrlAcceptsDomainAndApiPath() {
        assertThat(urlFactory.genericUrl("company.atlassian.net", "issue/ABC-1"))
                .isEqualTo("https://company.atlassian.net/rest/api/3/issue/ABC-1");
    }

    @Test
    void genericUrlDoesNotDuplicateRestApiPrefix() {
        assertThat(urlFactory.genericUrl("https://company.atlassian.net/", "/rest/api/3/search/jql"))
                .isEqualTo("https://company.atlassian.net/rest/api/3/search/jql");
    }

    @Test
    void configuredUrlUsesProvidedBaseUrl() {
        assertThat(urlFactory.configuredUrl("https://selected.atlassian.net", JiraApiEndpoint.PROJECT_ID_OR_KEY, "ABC"))
                .isEqualTo("https://selected.atlassian.net/rest/api/3/project/ABC");
    }

    @Test
    void normalizeBaseUrlAddsHttpsAndTrimsSlashes() {
        assertThat(urlFactory.normalizeBaseUrl(" company.atlassian.net/// "))
                .isEqualTo("https://company.atlassian.net");
    }

    @Test
    void genericUrlRejectsBlankDomainAndPath() {
        assertThatThrownBy(() -> urlFactory.genericUrl(" ", "/issue/ABC-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Jira domain or base URL is required");

        assertThatThrownBy(() -> urlFactory.genericUrl("company.atlassian.net", " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Jira API path is required");
    }

    @Test
    void configuredUrlRequiresProvidedBaseUrl() {
        assertThatThrownBy(() -> urlFactory.configuredUrl(" ", JiraApiEndpoint.PROJECT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Jira domain or base URL is required");
    }
}
