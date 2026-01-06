package com.spotlight.back.spotlight.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum FileUploadError implements ErrorCode {
    
    UPLOAD_FAILED("FILE-001", "File upload failed"),
    FILE_EMPTY("FILE-002", "File is empty"),
    NOT_FOUND("FILE-003", "File not found");
    
    private final String code;
    private final String description;
}
