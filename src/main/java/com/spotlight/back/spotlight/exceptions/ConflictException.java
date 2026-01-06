package com.spotlight.back.spotlight.exceptions;

import com.spotlight.back.spotlight.exceptions.errors.ErrorCode;

import lombok.Getter;

@Getter
public class ConflictException extends ErrorCodeException {

    public ConflictException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public ConflictException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
