package com.spotlight.back.spotlight.models.dtos;

import com.spotlight.back.spotlight.models.entities.UserTeam;

public class UserTeamDto {
    public String userId;
    public String teamId;
    public UserTeam.UserRole role;
}