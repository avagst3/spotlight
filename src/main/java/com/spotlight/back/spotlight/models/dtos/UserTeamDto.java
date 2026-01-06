package com.spotlight.back.spotlight.models.dtos;

import java.util.UUID;

import com.spotlight.back.spotlight.models.entities.UserTeam;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamDto {
    @NotNull
    public UUID userId;
    @NotNull
    public UUID teamId;
    @NotNull
    public UserTeam.UserRole role;
}