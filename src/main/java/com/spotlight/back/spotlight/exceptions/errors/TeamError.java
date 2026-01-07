package com.spotlight.back.spotlight.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum TeamError implements ErrorCode{
    TEAM_NOT_FOUND("TEAM-001", "Team not found");

    private final String code;
    private final String description;
}
