package com.myatos.net.project.repository;

import com.myatos.net.project.model.ProjectIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectIssueRepository extends JpaRepository<ProjectIssue, Long> {
    List<ProjectIssue> findByProject_Id(Long projectId);
    long countByProject_Id(Long projectId);
}
