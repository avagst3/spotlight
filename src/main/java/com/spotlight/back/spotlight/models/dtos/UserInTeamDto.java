package com.spotlight.back.spotlight.models.dtos;

import java.util.UUID;

import com.spotlight.back.spotlight.models.entities.UserTeam.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInTeamDto {
    @NotNull
    private UUID id;
    @NotNull
    private String name;
    private String profilePictureUrl;
    @NotNull
    private UserRole role;
    
}
