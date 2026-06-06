package com.myatos.net.mcp.config;

import com.myatos.net.mcp.tool.JiraMcpTools;
import com.myatos.net.mcp.tool.GitHubCopilotMcpTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolConfiguration {

    @Bean
    ToolCallbackProvider jiraToolCallbackProvider(JiraMcpTools jiraMcpTools, GitHubCopilotMcpTools gitHubCopilotMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(jiraMcpTools, gitHubCopilotMcpTools)
                .build();
    }
}
