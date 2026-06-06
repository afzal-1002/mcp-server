package com.myatos.net.project.repository;

import com.myatos.net.project.model.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByKey(String key);
    Optional<Project> findByJiraIdAndBaseUrl(String jiraId, String baseUrl);

    @EntityGraph(attributePaths = "projectUsers")
    Optional<Project> findByKeyAndBaseUrl(String key, String baseUrl);

    @EntityGraph(attributePaths = "projectUsers")
    List<Project> findAllByBaseUrl(String baseUrl);


}
