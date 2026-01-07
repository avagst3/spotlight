package com.spotlight.back.spotlight.models.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectInTeamDto {
    @NotEmpty
    public UUID id;
    @NotEmpty
    public String name;
    @NotEmpty
    public String description;
}
