package com.spotlight.back.spotlight.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum JwtErrorCode implements ErrorCode {
    PRIVATE_KEY_INVALID("JWT-001", "Private key is invalid"),
    PUBLIC_KEY_INVALID("JWT-002", "Public key is invalid"),
    TOKEN_INVALID("JWT-003", "Token is invalid"),
    TOKEN_EXPIRED("JWT-004", "Token is expired");

    private final String code;
    private final String description;
}
