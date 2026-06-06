package com.myatos.net.issues.service;

import com.myatos.net.issues.dto.issue.attachment.DownloadedFile;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface AttachmentService {

    // first attachment only
    DownloadedFile downloadFirstAttachmentWithWeb(String issueKey);

    // all attachments
    List<DownloadedFile> downloadAllAttachmentsWithWeb(String issueKey);

    DownloadedFile downloadFirstAttachmentWithRest(String issueKey);
    List<DownloadedFile> downloadAllAttachmentsWithRest(String issueKey);


    public DownloadedFile downloadAttachment(String issueKey) ;
    List<DownloadedFile> downloadAllAttachments(String issueKey);
}

