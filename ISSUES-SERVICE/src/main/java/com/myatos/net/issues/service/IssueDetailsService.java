package com.myatos.net.issues.service;

import com.myatos.net.issues.dto.issue.issuedetails.IssueDetails;
import org.springframework.stereotype.Service;

@Service
public interface IssueDetailsService {

    IssueDetails getIssueDetails(String issueKey);
}
