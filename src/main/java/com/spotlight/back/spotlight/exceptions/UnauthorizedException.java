package com.spotlight.back.spotlight.exceptions;

import com.spotlight.back.spotlight.exceptions.errors.ErrorCode;

import lombok.Getter;

@Getter
public class UnauthorizedException extends ErrorCodeException {

    public UnauthorizedException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public UnauthorizedException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
