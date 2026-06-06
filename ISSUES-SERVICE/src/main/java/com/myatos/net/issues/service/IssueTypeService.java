package com.myatos.net.issues.service;

import com.myatos.net.issues.dto.issuetype.CreateIssueTypeRequest;
import com.myatos.net.issues.dto.issuetype.CreateIssueTypeResponse;
import com.myatos.net.issues.model.issuetype.IssueType;
import com.myatos.net.issues.dto.issuetype.IssueTypeReference;

import java.util.List;
import java.util.Optional;

public interface IssueTypeService {
    CreateIssueTypeResponse createIssueType(CreateIssueTypeRequest request);
    List<IssueType> syncAllIssueTypes();
    Optional<IssueType> find(IssueTypeReference reference);
    Optional<IssueType> findById(Long id);
    Optional<IssueType> findByName(String name);
    IssueType findOrCreateIssueType(IssueType jiraIssueType, String issueKey);

    List<IssueType> getAllIssueTypesLocal();
    List<IssueType> getAllIssueTypesJira();
    IssueType getIssueTypeById(Long issueTypeId);
    IssueType getIssueTypeByName(String issueTypeName);
    List<IssueType> getAllIssueTypesForProject(Long projectId);
    CreateIssueTypeResponse updateIssueType(Long issueTypeId, CreateIssueTypeRequest request);
    void deleteIssueType(Long issueTypeId);

    IssueType saveIfNotExistsOrUpdate(IssueType issueType);

    public IssueType findOrCreateIssueTypeByName(String name);

    IssueType getById(String jiraId);
    IssueType getByNameIgnoreCase(String name);
    IssueType findOrCreateBIdOrName(String jiraId, String name);

}