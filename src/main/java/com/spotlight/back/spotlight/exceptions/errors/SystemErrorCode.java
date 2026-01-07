package com.spotlight.back.spotlight.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum SystemErrorCode implements ErrorCode {
    TECHNICAL_ERROR("SYS-001", "Technical error occurred");

    private final String code;
    private final String description;
}
