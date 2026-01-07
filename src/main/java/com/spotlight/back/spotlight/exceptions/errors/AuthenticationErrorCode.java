package com.spotlight.back.spotlight.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum AuthenticationErrorCode implements ErrorCode {
    JWT_INVALID("AUTH-001", "Invalid JWT Token"),
    JWT_EXPIRED("AUTH-002", "JWT Token has expired"),
    FAILED("AUTH-003", "Authentication failed");

    private final String code;
    private final String description;
}
