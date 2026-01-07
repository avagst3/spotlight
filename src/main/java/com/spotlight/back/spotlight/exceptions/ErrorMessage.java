package com.spotlight.back.spotlight.exceptions;

import java.time.OffsetDateTime;

import com.spotlight.back.spotlight.exceptions.errors.ErrorCode;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorMessage implements ErrorCode{
    private final String code;
    private final String description;
    private final OffsetDateTime timestamp;

    public static ErrorMessage fromErrorCodeException(ErrorCodeException ex) {
        return new ErrorMessage(
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getDescription(),
                OffsetDateTime.now());
    }

    public static ErrorMessage fromErrorCode(ErrorCode errorCode, Object... args) {
        return new ErrorMessage(
                errorCode.getCode(),
                String.format(errorCode.getDescription(), args),
                OffsetDateTime.now());
    }
    
}
