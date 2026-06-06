package com.myatos.net.mcp;

import com.myatos.net.mcp.config.JiraMcpProperties;
import com.myatos.net.mcp.config.GitHubCopilotProperties;
import com.myatos.net.mcp.config.McpServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JiraMcpProperties.class, GitHubCopilotProperties.class, McpServiceProperties.class})
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
}
