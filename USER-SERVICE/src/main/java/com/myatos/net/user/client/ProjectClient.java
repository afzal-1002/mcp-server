package com.myatos.net.user.client;

import com.myatos.net.user.configuration.FeignSecurityConfiguration;
import com.myatos.net.user.dto.project.ProjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(
        name = "PROJECT-SERVICE",
        contextId = "ProjectClient",
        path = "/api/wut/projects",
        configuration = FeignSecurityConfiguration.class
)
public interface ProjectClient {

    @PostMapping("/sync/all")
    List<ProjectResponse> syncAllFromJira();
}
