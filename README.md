# mcp-server

Spring Boot microservices backend for Jira, project repository mapping, and GitHub Copilot MCP orchestration.

## Modules

- `SERVICE-REGISTRY` - Eureka service registry
- `API-GATEWAY` - gateway routes for backend APIs
- `USER-SERVICE` - users, Jira sites, Jira credentials, GitHub integration credentials
- `PROJECT-SERVICE` - Jira projects and project GitHub repository mappings
- `ISSUES-SERVICE` - Jira issue/comment integration
- `MCP-SERVER` - dynamic Jira/GitHub Copilot MCP tools and REST endpoints
