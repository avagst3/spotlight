package com.spotlight.back.spotlight.exceptions.errors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum ProjectError implements ErrorCode {
    PROJECT_NOT_FOUND("PROJECT-001", "Project not found");

    private final String code;
    private final String description;
}
