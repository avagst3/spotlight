package com.spotlight.back.spotlight.exceptions;

import com.spotlight.back.spotlight.exceptions.errors.ErrorCode;

import lombok.Getter;

@Getter
public class NotFoundException extends ErrorCodeException {

    public NotFoundException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(cause, errorCode, args);
    }

    public NotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
