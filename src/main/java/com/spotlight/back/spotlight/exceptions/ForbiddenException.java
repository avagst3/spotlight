package com.spotlight.back.spotlight.exceptions;

import com.spotlight.back.spotlight.exceptions.errors.ErrorCode;

import lombok.Getter;

@Getter
public class ForbiddenException extends ErrorCodeException {

    public ForbiddenException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public ForbiddenException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
