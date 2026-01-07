package com.spotlight.back.spotlight.exceptions.errors;

public interface ErrorCode {
    String getCode();
    String getDescription();

    default String printableError() {
        return String.join("-", getCode(), getDescription());
    }
}
