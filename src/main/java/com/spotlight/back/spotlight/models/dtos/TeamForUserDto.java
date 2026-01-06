package com.spotlight.back.spotlight.models.dtos;

import java.util.List;
import java.util.UUID;

import com.spotlight.back.spotlight.models.entities.UserTeam.UserRole;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamForUserDto {
    @NotNull
    public UUID id;
    @NotEmpty
    public String name;
    public String description;
    public String profilePictureUrl;
    public UserRole role;
    public List<ProjectInTeamDto> projects;
}
