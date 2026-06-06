package com.myatos.net.issues.dto.issue.attachment;

public record DownloadedFile(
        byte[] bytes,
        String contentType,
        String filename,
        long contentLength
) {}
