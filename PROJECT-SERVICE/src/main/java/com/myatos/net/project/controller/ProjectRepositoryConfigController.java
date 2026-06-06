package com.myatos.net.project.controller;

import com.myatos.net.project.dto.project.CreateProjectRepositoryRequest;
import com.myatos.net.project.dto.project.ProjectRepositoryResponse;
import com.myatos.net.project.dto.project.UpdateProjectRepositoryRequest;
import com.myatos.net.project.service.ProjectRepositoryConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wut/projects/{projectKey}/repositories")
@RequiredArgsConstructor
public class ProjectRepositoryConfigController {

    private final ProjectRepositoryConfigService projectRepositoryConfigService;

    @PostMapping
    public ResponseEntity<ProjectRepositoryResponse> create(
            @PathVariable String projectKey,
            @RequestBody CreateProjectRepositoryRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectRepositoryConfigService.create(projectKey, request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectRepositoryResponse>> list(
            @PathVariable String projectKey,
            @RequestParam(name = "baseUrl", required = false) String baseUrl
    ) {
        return ResponseEntity.ok(projectRepositoryConfigService.listByProject(projectKey, baseUrl));
    }

    @GetMapping("/default")
    public ResponseEntity<ProjectRepositoryResponse> getDefault(
            @PathVariable String projectKey,
            @RequestParam(name = "baseUrl", required = false) String baseUrl
    ) {
        return ResponseEntity.ok(projectRepositoryConfigService.getDefaultRepository(projectKey, baseUrl));
    }

    @GetMapping("/{repositoryId}")
    public ResponseEntity<ProjectRepositoryResponse> get(
            @PathVariable String projectKey,
            @PathVariable Long repositoryId,
            @RequestParam(name = "baseUrl", required = false) String baseUrl
    ) {
        return ResponseEntity.ok(projectRepositoryConfigService.getRepository(projectKey, repositoryId, baseUrl));
    }

    @PutMapping("/{repositoryId}")
    public ResponseEntity<ProjectRepositoryResponse> update(
            @PathVariable String projectKey,
            @PathVariable Long repositoryId,
            @RequestParam(name = "baseUrl", required = false) String baseUrl,
            @RequestBody UpdateProjectRepositoryRequest request
    ) {
        return ResponseEntity.ok(projectRepositoryConfigService.update(projectKey, repositoryId, baseUrl, request));
    }

    @DeleteMapping("/{repositoryId}")
    public ResponseEntity<Void> delete(
            @PathVariable String projectKey,
            @PathVariable Long repositoryId,
            @RequestParam(name = "baseUrl", required = false) String baseUrl
    ) {
        projectRepositoryConfigService.delete(projectKey, repositoryId, baseUrl);
        return ResponseEntity.noContent().build();
    }
}
