package com.spotlight.back.spotlight.models.dtos;

import java.util.List;
import java.util.UUID;

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
public class TeamResponceDto {
    @NotNull
    public UUID id;
    @NotEmpty
    public String name;
    @NotEmpty
    public String description;
    public String profilePictureUrl;
    public List<UserInTeamDto> users;
    public List<ProjectInTeamDto> projects;
}
