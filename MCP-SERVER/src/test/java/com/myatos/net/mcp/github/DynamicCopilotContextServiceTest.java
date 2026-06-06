package com.myatos.net.mcp.github;

import com.myatos.net.mcp.client.ProjectServiceClient;
import com.myatos.net.mcp.client.UserServiceClient;
import com.myatos.net.mcp.client.dto.ProjectRepositoryResponse;
import com.myatos.net.mcp.client.dto.ResolvedIntegrationCredentialResponse;
import com.myatos.net.mcp.client.dto.UserCredentialResponse;
import com.myatos.net.mcp.config.JiraMcpProperties;
import com.myatos.net.mcp.jira.JiraUrlFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamicCopilotContextServiceTest {

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ProjectServiceClient projectServiceClient;

    @Test
    void resolvesJiraCredentialRepositoryAndGithubTokenFromServices() {
        DynamicCopilotContextService service = service();
        when(userServiceClient.getResolvedJiraCredential("https://jira.example.com"))
                .thenReturn(new UserCredentialResponse(1L, "jira@example.com", "account", "jira-token", "https://jira.example.com"));
        when(projectServiceClient.getDefaultRepository("ABC", "https://jira.example.com"))
                .thenReturn(new ProjectRepositoryResponse(
                        10L,
                        "ABC",
                        "Backend",
                        "https://github.gsissc.myatos.net/my-org/my-repo.git",
                        "develop",
                        20L,
                        true,
                        true
                ));
        when(userServiceClient.getResolvedIntegrationCredential(20L))
                .thenReturn(new ResolvedIntegrationCredentialResponse(20L, "GitHub", "GITHUB", "git-user", "gh-token", null));

        DynamicCopilotContext context = service.resolve("jira.example.com", "ABC", null, null);

        assertThat(context.jiraBaseUrl()).isEqualTo("https://jira.example.com");
        assertThat(context.jiraEmail()).isEqualTo("jira@example.com");
        assertThat(context.jiraApiToken()).isEqualTo("jira-token");
        assertThat(context.repositoryOwner()).isEqualTo("my-org");
        assertThat(context.repositoryName()).isEqualTo("my-repo");
        assertThat(context.targetRepository()).isEqualTo("my-org/my-repo");
        assertThat(context.baseBranch()).isEqualTo("develop");
        assertThat(context.githubToken()).isEqualTo("gh-token");
    }

    @Test
    void rejectsMissingDefaultRepository() {
        DynamicCopilotContextService service = service();
        when(userServiceClient.getResolvedJiraCredential("https://jira.example.com"))
                .thenReturn(new UserCredentialResponse(1L, "jira@example.com", "account", "jira-token", "https://jira.example.com"));
        when(projectServiceClient.getDefaultRepository("ABC", "https://jira.example.com")).thenReturn(null);

        assertThatThrownBy(() -> service.resolve("jira.example.com", "ABC", null, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No default GitHub repository configured for project: ABC");
    }

    private DynamicCopilotContextService service() {
        return new DynamicCopilotContextService(
                userServiceClient,
                projectServiceClient,
                new JiraUrlFactory(new JiraMcpProperties("3", 25)),
                new GitHubRepositoryUrlParser()
        );
    }
}
